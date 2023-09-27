package com.huiyu.service.core.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.config.executor.CompletableFutureExceptionHandle;
import com.huiyu.service.core.constant.RedisKeyEnum;
import com.huiyu.service.core.constant.RedisLockEnum;
import com.huiyu.service.core.convert.PointRecordConvert;
import com.huiyu.service.core.convert.UserConvert;
import com.huiyu.service.core.entity.Invite;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.entity.SignRecord;
import com.huiyu.service.core.enums.DailyTaskEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import com.huiyu.common.web.exception.BizException;
import com.huiyu.service.core.hconfig.config.HotFileConfig;
import com.huiyu.service.core.model.bo.UpdatePointHandlerBO;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.dto.UserPicCountDto;
import com.huiyu.service.core.model.dto.UserPicShareCountDto;
import com.huiyu.service.core.model.query.UserQuery;
import com.huiyu.service.core.model.vo.PointRecordPageVo;
import com.huiyu.service.core.model.vo.UserAdminVo;
import com.huiyu.service.core.model.vo.UserVo;
import com.huiyu.service.core.service.InviteService;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.PicShareService;
import com.huiyu.service.core.service.PointRecordService;
import com.huiyu.service.core.service.SignRecordService;
import com.huiyu.service.core.service.UserService;
import com.huiyu.service.core.service.business.UserBusiness;
import com.huiyu.service.core.service.extension.SecureCheckService;
import com.huiyu.service.core.utils.IdUtils;
import com.huiyu.service.core.utils.upload.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Naccl
 * @date 2023-08-11
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserBusinessImpl implements UserBusiness {
    private final UserService userService;
    private final PointRecordService pointRecordService;
    private final SignRecordService signRecordService;
    private final PicService picService;
    private final PicShareService picShareService;
    private final InviteService inviteService;
    private final SecureCheckService secureCheckService;
    private final HotFileConfig hotFileConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Redisson redisson;
    @Qualifier("registerExecutor")
    private final Executor registerExecutor;

    @Override
    public IPage<UserAdminVo> adminPageQuery(IPage<User> page, UserQuery query) {
        IPage<User> userPage = userService.adminPageQuery(page, query);

        IPage<UserAdminVo> userAdminVoPage = UserConvert.INSTANCE.toAdminVOPage(userPage);
        if (CollectionUtils.isEmpty(userPage.getRecords())) {
            return userAdminVoPage;
        }

        List<Long> userIdList = userAdminVoPage.getRecords().stream().map(UserAdminVo::getUserId).collect(Collectors.toList());

        // 图片数量
        List<UserPicCountDto> userPicCountDtoList = picService.countByUserIdList(userIdList);
        Map<Long, Integer> userPicCountMap = userPicCountDtoList.stream().collect(Collectors.toMap(UserPicCountDto::getUserId, UserPicCountDto::getPicCount, (k1, k2) -> k1));

        // 投稿数量
        List<UserPicShareCountDto> userPicShareCountDtoList = picShareService.countByUserIdList(userIdList);
        Map<Long, Integer> userPicShareCountMap = userPicShareCountDtoList.stream().collect(Collectors.toMap(UserPicShareCountDto::getUserId, UserPicShareCountDto::getPicShareCount, (k1, k2) -> k1));

        userAdminVoPage.getRecords().forEach(userAdminVo -> {
            Integer picCount = userPicCountMap.get(userAdminVo.getUserId());
            Integer picShareCount = userPicShareCountMap.get(userAdminVo.getUserId());
            userAdminVo.setPicCount(picCount == null ? 0 : picCount);
            userAdminVo.setPicShareCount(picShareCount == null ? 0 : picShareCount);
        });
        return userAdminVoPage;
    }

    @Override
    public User addUser(User user) {
        User resUser = userService.insert(user);

        CompletableFuture.runAsync(() -> {
                    // 注册赠送积分
                    Integer registerPoint = hotFileConfig.getInt("registerPoint", 100);
                    this.updatePoint(resUser.getUserId(), registerPoint, PointOperationSourceEnum.REGISTER, PointOperationTypeEnum.ADD, null, PointTypeEnum.POINT);
                }, registerExecutor)
                .exceptionally(CompletableFutureExceptionHandle.ExceptionLogHandle);

        return resUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(User user) {
        // 修改用户积分
        this.updatePoint(user.getUserId(), user.getPoint(), PointOperationSourceEnum.ADMIN_UPDATE, PointOperationTypeEnum.WAIT_CALC, null, PointTypeEnum.POINT);

        // 修改用户信息
        user.setPoint(null);
        return userService.update(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePoint(Long userId, Integer pointDiff, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid, PointTypeEnum pointType) {
        log.info("用户积分修改, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}", userId, pointDiff, source, operation, requestUuid, pointType);
        if (pointDiff == null || pointDiff <= 0) {
            throw new IllegalArgumentException("积分数值错误");
        }
        if (source == null) {
            throw new IllegalArgumentException("积分来源错误");
        }

        if (source == PointOperationSourceEnum.GENERATE_PIC) {
            // 生成图片前先执行每日签到
            this.signIn(userId);
        }

        Integer targetDailyPointDiff;
        Integer targetPointDiff;

        // 加锁
        String redisLockKey = RedisLockEnum.UPDATE_POINT.getFormatKey(String.valueOf(userId));
        RLock lock = redisson.getLock(redisLockKey);
        try {
            boolean isLocked = lock.tryLock(3, 2, TimeUnit.SECONDS);

            if (isLocked) {
                // 参数校验
                source.checkParam(operation, pointType);

                UpdatePointHandlerBO updatePointHandlerBO = source.updatePointHandler(userId, pointDiff, operation, pointType, requestUuid);
                if (updatePointHandlerBO == null) {
                    // 如果返回null，则取消积分修改
                    log.info("用户积分修改取消, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}", userId, pointDiff, source, operation, requestUuid, pointType);
                    return false;
                }

                targetDailyPointDiff = updatePointHandlerBO.getTargetDailyPointDiff();
                targetPointDiff = updatePointHandlerBO.getTargetPointDiff();
                operation = updatePointHandlerBO.getOperationType();
                pointType = updatePointHandlerBO.getPointType();

                log.info("更新用户积分, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
                // 修改用户积分
                boolean isUpdatePointOK = userService.updatePointByUserId(userId, targetDailyPointDiff, targetPointDiff);
                if (!isUpdatePointOK) {
                    log.error("更新用户积分失败, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
                    throw new BizException("异常错误");
                }
            } else {
                log.error("RedisLock: {}, 上锁超时", redisLockKey);
                throw new BizException("异常错误");
            }
        } catch (Exception e) {
            log.error("RedisLock: {}, 上锁期间发生异常", redisLockKey, e);
            throw new BizException("异常错误");
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        // 记录积分流水表
        PointRecord pointRecord = PointRecord.builder()
                .id(IdUtils.nextSnowflakeId())
                .userId(userId)
                .requestUuid(requestUuid == null ? "" : requestUuid)
                .dailyPoint(Math.abs(targetDailyPointDiff))
                .point(Math.abs(targetPointDiff))
                .operationType(operation)
                .operationSource(source)
                .pointType(pointType)
                .build();
        boolean isInsertPointRecordOK = pointRecordService.insertRecord(pointRecord);
        if (!isInsertPointRecordOK) {
            log.error("记录积分流水表失败, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
            throw new BizException("异常错误");
        }

        return true;
    }

    @Override
    public IPage<PointRecordPageVo> pagePointRecord(IPage<PointRecord> page, PointRecordPageDto dto) {
        Assert.notNull(dto.getUserId(), "异常错误");
        IPage<PointRecord> pointRecordPage = pointRecordService.pagePointRecord(page, dto);
        Page<PointRecordPageVo> pointRecordVoPage = PointRecordConvert.INSTANCE.toVOPage(pointRecordPage);
        return pointRecordVoPage;
    }

    @Override
    public UserVo getUserInfo(Long userId) {
        User user = userService.queryByUserId(userId);
        UserVo userVo = UserConvert.INSTANCE.toVo(user);
        // 每日任务
        userVo.setDailyTaskList(processDailyTaskList(userId));
        return userVo;
    }

    /**
     * 处理每日任务vo
     */
    private List<UserVo.DailyTask> processDailyTaskList(Long userId) {
        String dailyTaskRedisKey = RedisKeyEnum.DAILY_TASK_MAP.getFormatKey(LocalDate.now().toString(), String.valueOf(userId));

        List<UserVo.DailyTask> dailyTaskList = new ArrayList<>();
        redisTemplate.opsForHash().entries(dailyTaskRedisKey).forEach((k, v) -> {
            String key = (String) k;
            Integer value = (Integer) v;
            DailyTaskEnum dailyTaskEnum = DailyTaskEnum.getByDictKey(key);

            String desc = "";
            Boolean status = false;
            String action = "";

            if (dailyTaskEnum.getRoundPerDay() != -1) {
                // 每日可完成次数有限制
                if (dailyTaskEnum.getCountPerRound() != -1) {
                    if (dailyTaskEnum.getRoundPerDay() == 1) {
                        desc = String.format("进度(%d/%d)", value, dailyTaskEnum.getCountPerRound());
                    } else {
                        desc = String.format("进度(%d/%d)，可完成(%d/%d)", value % dailyTaskEnum.getCountPerRound(), dailyTaskEnum.getCountPerRound(), value / dailyTaskEnum.getCountPerRound(), dailyTaskEnum.getRoundPerDay());
                    }
                    if (value >= dailyTaskEnum.getCountPerRound() * dailyTaskEnum.getRoundPerDay()) {
                        status = true;
                        action = "已完成";
                    } else {
                        status = false;
                        action = dailyTaskEnum.getAction();
                    }
                } else {
                    // getCountPerRound为-1 触发即认为完成且不显示次数
                    if (dailyTaskEnum.getRoundPerDay() != 1) {
                        desc = String.format("可完成(%d/%d)", value, dailyTaskEnum.getRoundPerDay());
                    } else {
                        desc = "";
                    }
                    if (value >= dailyTaskEnum.getRoundPerDay()) {
                        status = true;
                        action = "已完成";
                    } else {
                        status = false;
                        action = dailyTaskEnum.getAction();
                    }
                }
            } else {
                // 每日可无限完成
                desc = "";
                status = false;
                action = dailyTaskEnum.getAction();
            }

            UserVo.DailyTask dailyTask = UserVo.DailyTask.builder()
                    .key(dailyTaskEnum.getDictKey())
                    .title(dailyTaskEnum.getDesc())
                    .desc(desc)
                    .point(dailyTaskEnum.getPoint())
                    .status(status)
                    .action(action)
                    .build();
            dailyTaskList.add(dailyTask);
        });
        return dailyTaskList;
    }

    @Override
    public boolean updateAvatar(Long userId, MultipartFile file) {
        String avatarUrl;
        try {
            avatarUrl = UploadUtils.uploadWithResizeCompress(file);
        } catch (Exception e) {
            log.error("上传头像失败", e);
            throw new BizException("上传头像失败");
        }

        log.info("头像上传成功, 调用微信图片审核接口");
        boolean checkRes = secureCheckService.checkImage(avatarUrl);
        if (!checkRes) {
            throw new BizException("头像检测违规，修改失败");
        }

        return userService.updateAvatar(userId, avatarUrl);
    }

    @Override
    public boolean updateNickname(Long userId, String nickname) {
        nickname = StringUtils.trimToEmpty(nickname);
        if (StringUtils.isBlank(nickname) || nickname.length() > 8) {
            throw new BizException("昵称无效");
        }

        log.info("修改昵称, 调用微信文本审核接口");
        boolean checkRes = secureCheckService.checkMessage(nickname);
        if (!checkRes) {
            throw new BizException("昵称包含违规内容，修改失败");
        }

        return userService.updateNickname(userId, nickname);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean signIn(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = now.toLocalDate();

        // 判断用户是否已签到
        String dailyTaskRedisKey = RedisKeyEnum.DAILY_TASK_MAP.getFormatKey(nowDate.toString(), String.valueOf(userId));
        Integer o = (Integer) redisTemplate.opsForHash().get(dailyTaskRedisKey, DailyTaskEnum.SIGN_IN.getDictKey());
        if (o != null && o == 1) {
            log.info("今日已签到 userId: {}", userId);
            return false;
        }

        if (signRecordService.isSignIn(userId, nowDate)) {
            return false;
        }

        // 记录每日任务完成情况
        redisTemplate.opsForHash().put(dailyTaskRedisKey, DailyTaskEnum.SIGN_IN.getDictKey(), 1);
        // 初始化其它每日任务
        this.initDailyTaskRedis(dailyTaskRedisKey);

        log.info("记录每日任务完成情况, desc: {}, dailyTaskRedisKey: {}", DailyTaskEnum.SIGN_IN.getDesc(), dailyTaskRedisKey);

        // 签到记录
        SignRecord signRecord = SignRecord.builder()
                .userId(userId)
                .signDate(nowDate)
                .signTime(now)
                .isDelete(0)
                .build();
        try {
            signRecordService.save(signRecord);
        } catch (DuplicateKeyException e) {
            log.warn("今日已签到 signRecord: {}", JacksonUtils.toJsonStr(signRecord));
            return false;
        }

        // 签到积分奖励
        Integer signInPoint = DailyTaskEnum.SIGN_IN.getPointByHotFile();
        if (signInPoint > 0) {
            log.info("奖励积分, desc: {}, userId: {}, point: {}", DailyTaskEnum.SIGN_IN.getDesc(), userId, signInPoint);
            this.updatePoint(userId, signInPoint, PointOperationSourceEnum.SIGN_IN, PointOperationTypeEnum.ADD, null, PointTypeEnum.DAILY_POINT);
        }

        return true;
    }

    private void initDailyTaskRedis(String dailyTaskRedisKey) {
        for (DailyTaskEnum value : DailyTaskEnum.values()) {
            if (value.getDictKey().equals(DailyTaskEnum.SIGN_IN.getDictKey())) {
                continue;
            }
            redisTemplate.opsForHash().putIfAbsent(dailyTaskRedisKey, value.getDictKey(), 0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dailyTaskFinished(Long userId, DailyTaskEnum dailyTaskEnum) {
        String dailyTaskRedisKey = RedisKeyEnum.DAILY_TASK_MAP.getFormatKey(LocalDate.now().toString(), String.valueOf(userId));
        // 判断今日是否已完成
        Integer finishedCount = (Integer) redisTemplate.opsForHash().get(dailyTaskRedisKey, dailyTaskEnum.getDictKey());

        log.info("记录每日任务完成情况, desc: {}, dailyTaskRedisKey: {}, finishedCount: {}", dailyTaskEnum.getDesc(), dailyTaskRedisKey, finishedCount);

        if (finishedCount != null && dailyTaskEnum.getRoundPerDay() != -1) {
            // 每日可完成轮数有限，则判断今日已完成几轮
            if (dailyTaskEnum.getCountPerRound() == -1) {
                if (finishedCount >= dailyTaskEnum.getRoundPerDay()) {
                    // 今日轮数已全部完成
                    return;
                }
            } else {
                if (finishedCount >= dailyTaskEnum.getCountPerRound() * dailyTaskEnum.getRoundPerDay()) {
                    // 今日轮数已全部完成
                    return;
                }
            }
        }
        Long increment = redisTemplate.opsForHash().increment(dailyTaskRedisKey, dailyTaskEnum.getDictKey(), 1);
        if (dailyTaskEnum.getCountPerRound() == -1 || (increment != 0 && increment % dailyTaskEnum.getCountPerRound() == 0)) {
            // 奖励积分
            Integer point = dailyTaskEnum.getPointByHotFile();

            log.info("奖励积分, desc: {}, userId: {}, point: {}", dailyTaskEnum.getDesc(), userId, point);
            this.updatePoint(userId, point, PointOperationSourceEnum.getByDictKey("dailyTask_" + dailyTaskEnum.getDictKey()), PointOperationTypeEnum.ADD, null, PointTypeEnum.POINT);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean bindInviter(Long sharerUserId, Long userId) {
        Invite one = inviteService.getByInviteeId(userId);
        if (one != null) {
            log.info("已绑定邀请关系, 邀请人: {}, 被邀请人: {}", one.getInviterId(), userId);
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        Invite invite = Invite.builder()
                .inviterId(sharerUserId)
                .inviteeId(userId)
                .createTime(now)
                .updateTime(now)
                .build();

        boolean insertRes = inviteService.insert(invite);

        // 邀请人奖励积分
        if (insertRes) {
            this.updatePoint(sharerUserId, DailyTaskEnum.INVITE_USER.getPointByHotFile(), PointOperationSourceEnum.INVITE_USER, PointOperationTypeEnum.ADD, null, PointTypeEnum.POINT);
        }

        // TODO 被邀请人奖励积分
        return true;
    }
}
