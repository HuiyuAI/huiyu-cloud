package com.huiyu.service.core.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.constant.RedisKeyEnum;
import com.huiyu.service.core.convert.PointRecordConvert;
import com.huiyu.service.core.convert.UserConvert;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.entity.SignRecord;
import com.huiyu.service.core.enums.DailyTaskEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import com.huiyu.common.web.exception.BizException;
import com.huiyu.service.core.hconfig.config.HotFileConfig;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.vo.PointRecordPageVo;
import com.huiyu.service.core.model.vo.UserVo;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private final SecureCheckService secureCheckService;
    private final HotFileConfig hotFileConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(User user) {
        User one = userService.queryByUserId(user.getUserId());

        int diffPoint = user.getPoint() - one.getPoint();

        PointOperationTypeEnum operationType;
        if (diffPoint == 0) {
            return userService.update(user);
        } else if (diffPoint > 0) {
            operationType = PointOperationTypeEnum.ADD;
        } else {
            operationType = PointOperationTypeEnum.REDUCE;
        }

        LocalDateTime now = LocalDateTime.now();
        PointRecord pointRecord = PointRecord.builder()
                .id(IdUtils.nextSnowflakeId())
                .userId(user.getUserId())
                .requestUuid("")
                .point(Math.abs(diffPoint))
                .operationType(operationType)
                .operationSource(PointOperationSourceEnum.ADMIN_UPDATE)
                .pointType(PointTypeEnum.POINT)
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
        boolean isInsertPointRecordOK = pointRecordService.insertRecord(pointRecord);
        if (!isInsertPointRecordOK) {
            log.error("记录积分流水表失败，pointRecord: {}", JacksonUtils.toJsonStr(pointRecord));
            throw new BizException("异常错误");
        }

        return userService.update(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePoint(Long userId, Integer pointDiff, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid, PointTypeEnum pointType) {
        if (pointDiff == null || pointDiff <= 0) {
            throw new IllegalArgumentException("积分数值不合法");
        }
        if (pointType == null && (source != PointOperationSourceEnum.GENERATE_PIC && source != PointOperationSourceEnum.FAIL_RETURN)) {
            // 操作来源不是[生成图片]或[失败返还]时，积分类型不能为空
            throw new IllegalArgumentException("积分类型不合法");
        }

        User user = userService.queryByUserId(userId);

        Integer dailyPoint = user.getDailyPoint();
        Integer point = user.getPoint();

        // 要增减的每日积分、永久积分
        Integer targetDailyPointDiff = 0;
        Integer targetPointDiff = 0;

        if (operation == PointOperationTypeEnum.REDUCE) {
            // 判断用户积分是否足够
            if (dailyPoint + point < pointDiff) {
                throw new BizException("积分不足");
            }
        }

        if (pointType == null) {
            switch (source) {
                case GENERATE_PIC:
                    // 到这里时，总积分肯定足够
                    // 先判断每日积分是否为0
                    if (dailyPoint > 0) {
                        // 每日积分不为0，判断每日积分是否足够
                        if (dailyPoint >= pointDiff) {
                            // 每日积分足够，直接扣除每日积分
                            pointType = PointTypeEnum.DAILY_POINT;
                            targetDailyPointDiff = -pointDiff;
                        } else {
                            // 每日积分不够，扣除每日积分后，再扣除永久积分
                            pointType = PointTypeEnum.MIX_POINT;
                            targetDailyPointDiff = -dailyPoint;
                            targetPointDiff = -(pointDiff - dailyPoint);
                        }
                    } else {
                        // 每日积分为0，直接扣除永久积分
                        pointType = PointTypeEnum.POINT;
                        targetPointDiff = -pointDiff;
                    }
                    break;
                case FAIL_RETURN:
                    // 返还积分，先根据requestUuid查询积分流水表
                    PointRecord pointRecord = pointRecordService.getByRequestUuid(requestUuid);
                    if (pointRecord == null) {
                        // 按代码执行顺序，这里肯定能查到，查不到就是task执行前代码异常了
                        log.error("积分流水表查询失败, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, pointRecord: {}", userId, pointDiff, source, operation, requestUuid, pointType, JacksonUtils.toJsonStr(pointRecord));
                        return false;
                    }
                    // 判断是否使用了永久积分，且永久积分是否还有剩余返还数值
                    if (pointRecord.getPoint() > 0 && pointRecord.getPoint() - pointRecord.getReturnPoint() > 0) {
                        // 还有可返还的永久积分，判断剩余数值是否足够
                        if (pointRecord.getPoint() - pointRecord.getReturnPoint() >= pointDiff) {
                            // 剩余返还数值足够，直接返还永久积分
                            pointType = PointTypeEnum.POINT;
                            targetPointDiff = pointDiff;
                            // 更新积分流水表，记录返还数值
                            pointRecord.setReturnPoint(pointRecord.getReturnPoint() + pointDiff);
                            pointRecordService.updatePointRecord(pointRecord);
                        } else {
                            // 剩余返还数值不够，先返还永久积分，再返还每日积分
                            pointType = PointTypeEnum.MIX_POINT;
                            targetPointDiff = pointRecord.getPoint() - pointRecord.getReturnPoint();
                            targetDailyPointDiff = pointDiff - (pointRecord.getPoint() - pointRecord.getReturnPoint());
                            // 更新积分流水表，记录返还数值
                            pointRecord.setReturnPoint(pointRecord.getReturnPoint() + targetPointDiff);
                            pointRecord.setReturnDailyPoint(pointRecord.getReturnDailyPoint() + targetDailyPointDiff);
                            pointRecordService.updatePointRecord(pointRecord);
                        }
                    } else {
                        // 无可返还的永久积分，判断每日积分是否还有剩余返还数值
                        if (pointRecord.getDailyPoint() > 0 && pointRecord.getDailyPoint() - pointRecord.getReturnDailyPoint() > 0) {
                            // 还有可返还的每日积分，判断剩余数值是否足够
                            if (pointRecord.getDailyPoint() - pointRecord.getReturnDailyPoint() >= pointDiff) {
                                // 剩余返还数值足够，直接返还每日积分
                                pointType = PointTypeEnum.DAILY_POINT;
                                targetDailyPointDiff = pointDiff;
                                // 更新积分流水表，记录返还数值
                                pointRecord.setReturnDailyPoint(pointRecord.getReturnDailyPoint() + pointDiff);
                                pointRecordService.updatePointRecord(pointRecord);
                            } else {
                                log.error("积分流水表中剩余可返回每日积分不足, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, pointRecord: {}", userId, pointDiff, source, operation, requestUuid, pointType, JacksonUtils.toJsonStr(pointRecord));
                                return false;
                            }
                        } else {
                            // 每日积分和永久积分都没有剩余返还数值
                            log.error("积分流水表中没有可返还的积分, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, pointRecord: {}", userId, pointDiff, source, operation, requestUuid, pointType, JacksonUtils.toJsonStr(pointRecord));
                            return false;
                        }
                    }
                    break;
            }
        }

        log.info("更新用户积分, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
        // 修改用户积分
        boolean isUpdatePointOK = userService.updatePointByUserId(userId, targetDailyPointDiff, targetPointDiff);
        if (!isUpdatePointOK) {
            log.error("更新用户积分失败, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
            throw new BizException("异常错误");
        }

        // 关联请求uuid
        requestUuid = requestUuid == null ? "" : requestUuid;

        // 记录积分表
        LocalDateTime now = LocalDateTime.now();
        PointRecord pointRecord = PointRecord.builder()
                .id(IdUtils.nextSnowflakeId())
                .userId(userId)
                .requestUuid(requestUuid)
                .dailyPoint(Math.abs(targetDailyPointDiff))
                .point(Math.abs(targetPointDiff))
                .operationType(operation)
                .operationSource(source)
                .pointType(pointType)
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
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
        return UserConvert.INSTANCE.toVo(user);
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
        String dailyTaskRedisKey = RedisKeyEnum.DAILY_TASK_MAP.getFormatKey(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), String.valueOf(userId));

        LocalDateTime todayStart = now.with(LocalTime.MIN);
        LocalDateTime todayEnd = now.with(LocalTime.MAX);

        // 判断用户是否已签到
        if (signRecordService.isSignIn(userId, todayStart, todayEnd)) {
            return false;
        }

        // 签到记录
        SignRecord signRecord = SignRecord.builder()
                .userId(userId)
                .signTime(now)
                .isDelete(0)
                .build();
        signRecordService.save(signRecord);

        // 签到积分奖励
        User user = userService.queryByUserId(userId);
        Integer userDailyPoint = user.getDailyPoint();
        Integer signInPoint = hotFileConfig.getSignInPoint();
        Integer diffPoint = signInPoint - userDailyPoint;
        if (diffPoint > 0) {
            this.updatePoint(userId, diffPoint, PointOperationSourceEnum.SIGN_IN, PointOperationTypeEnum.ADD, null, PointTypeEnum.DAILY_POINT);
        }

        // 记录每日任务完成情况
        redisTemplate.opsForHash().put(dailyTaskRedisKey, DailyTaskEnum.SIGN_IN.getDictKey(), 1);

        return true;
    }

}
