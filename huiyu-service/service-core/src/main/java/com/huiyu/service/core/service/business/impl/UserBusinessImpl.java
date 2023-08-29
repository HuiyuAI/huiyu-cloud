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
import com.huiyu.service.core.model.bo.UpdatePointHandlerBO;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        User oldUser = userService.queryByUserId(user.getUserId());

        int diffPoint = user.getPoint() - oldUser.getPoint();

        PointOperationTypeEnum operationType;
        if (diffPoint == 0) {
            return userService.update(user);
        } else if (diffPoint > 0) {
            operationType = PointOperationTypeEnum.ADD;
        } else {
            operationType = PointOperationTypeEnum.REDUCE;
        }

        // 修改用户积分
        this.updatePoint(user.getUserId(), Math.abs(diffPoint), PointOperationSourceEnum.ADMIN_UPDATE, operationType, "", PointTypeEnum.POINT);

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
        source.checkParam(operation, pointType);

        UpdatePointHandlerBO updatePointHandlerBO = source.updatePointHandler(userId, pointDiff, pointType, requestUuid);
        Integer targetDailyPointDiff = updatePointHandlerBO.getTargetDailyPointDiff();
        Integer targetPointDiff = updatePointHandlerBO.getTargetPointDiff();
        pointType = updatePointHandlerBO.getPointType();

        log.info("更新用户积分, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
        // 修改用户积分
        boolean isUpdatePointOK = userService.updatePointByUserId(userId, targetDailyPointDiff, targetPointDiff);
        if (!isUpdatePointOK) {
            log.error("更新用户积分失败, userId: {}, pointDiff: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, targetDailyPointDiff: {}, targetPointDiff: {}", userId, pointDiff, source, operation, requestUuid, pointType, targetDailyPointDiff, targetPointDiff);
            throw new BizException("异常错误");
        }

        // 记录积分表
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

        LocalDate nowDate = now.toLocalDate();
        // 判断用户是否已签到
        if (signRecordService.isSignIn(userId, nowDate)) {
            return false;
        }

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
        User user = userService.queryByUserId(userId);
        Integer userDailyPoint = user.getDailyPoint();
        Integer signInPoint = hotFileConfig.getSignInPoint();
        Integer diffPoint = signInPoint - userDailyPoint;
        if (diffPoint > 0) {
            this.updatePoint(userId, diffPoint, PointOperationSourceEnum.SIGN_IN, PointOperationTypeEnum.ADD, null, PointTypeEnum.DAILY_POINT);
        }

        // 记录每日任务完成情况
        String dailyTaskRedisKey = RedisKeyEnum.DAILY_TASK_MAP.getFormatKey(nowDate.toString(), String.valueOf(userId));
        redisTemplate.opsForHash().put(dailyTaskRedisKey, DailyTaskEnum.SIGN_IN.getDictKey(), 1);

        return true;
    }

}
