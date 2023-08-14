package com.huiyu.service.core.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.convert.PointRecordConvert;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import com.huiyu.service.core.exception.BizException;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.vo.PointRecordPageVo;
import com.huiyu.service.core.service.PointRecordService;
import com.huiyu.service.core.service.UserService;
import com.huiyu.service.core.service.business.UserBusiness;
import com.huiyu.service.core.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
                .num(Math.abs(diffPoint))
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
    public boolean updatePoint(Long userId, Integer dailyPoint, Integer point, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid, PointTypeEnum pointType, Task task) {
        if ((dailyPoint == null && point == null) || (dailyPoint <= 0 && point <= 0)) {
            throw new IllegalArgumentException("积分数值不合法");
        }
        if (pointType == null && operation != PointOperationTypeEnum.REDUCE) {
            // 操作类型不是减少积分时，积分类型不能为空
            throw new IllegalArgumentException("积分类型不合法");
        }

        User user = userService.queryByUserId(userId);

        Integer dailyPoint = user.getDailyPoint();
        Integer point1 = user.getPoint();

        if (operation == PointOperationTypeEnum.REDUCE) {
            // 判断用户积分是否足够
            if (dailyPoint + point1 < point) {
                throw new BizException("积分不足");
            }

            point = -point;
        }

        if (pointType == null) {
            // 到这里时，操作类型只能是减少积分，且总积分肯定足够
            // 先判断每日积分是否为0
            if (dailyPoint > 0) {
                // 每日积分不为0，判断每日积分是否足够
                if (dailyPoint + point >= 0) {
                    // 每日积分足够，直接扣除每日积分
                    pointType = PointTypeEnum.DAILY_POINT;
                } else {
                    // 每日积分不够，扣除每日积分后，再扣除永久积分
                    pointType = PointTypeEnum.MIX_POINT;
                }
            } else {
                // 每日积分为0，直接扣除永久积分
                pointType = PointTypeEnum.POINT;
            }
        }

        log.info("更新用户积分，userId: {}, point: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, task: {}", userId, point, source, operation, requestUuid, pointType, JacksonUtils.toJsonStr(task));
        // 修改用户积分
        boolean isUpdatePointOK = userService.updatePointByUserId(userId, point, pointType == PointTypeEnum.DAILY_POINT);
        if (!isUpdatePointOK) {
            log.error("更新用户积分失败，userId: {}, point: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, task: {}", userId, point, source, operation, requestUuid, pointType, JacksonUtils.toJsonStr(task));
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
                .num(Math.abs(point))
                .operationType(operation)
                .operationSource(source)
                .pointType(pointType)
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
        boolean isInsertPointRecordOK = pointRecordService.insertRecord(pointRecord);
        if (!isInsertPointRecordOK) {
            log.error("记录积分流水表失败，userId: {}, point: {}, source: {}, operation: {}, requestUuid: {}, pointType: {}, task: {}", userId, point, source, operation, requestUuid, pointType, JacksonUtils.toJsonStr(task));
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
}
