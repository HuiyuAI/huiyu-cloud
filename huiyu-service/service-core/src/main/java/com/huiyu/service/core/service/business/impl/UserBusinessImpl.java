package com.huiyu.service.core.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.convert.PointRecordConvert;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
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

    /**
     * 更新积分
     *
     * @param userId      更新的用户id
     * @param point       需要操作的积分数值
     * @param source      操作来源
     * @param operation   积分增减
     * @param requestUuid 请求uuid
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePoint(Long userId, Integer point, PointOperationSourceEnum source, PointOperationTypeEnum operation, String requestUuid) {
        if (point == null || point <= 0) {
            throw new IllegalArgumentException("积分数值不合法");
        }

        if (operation == PointOperationTypeEnum.REDUCE) {
            point = -point;
        }

        // 修改用户积分
        boolean isUpdatePointOK = userService.updatePointByUserId(userId, point);
        if (!isUpdatePointOK) {
            log.error("更新用户积分失败，userId：{}，point：{}, source：{}, operation：{}", userId, point, source, operation);
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
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
        boolean isInsertPointRecordOK = pointRecordService.insertRecord(pointRecord);
        if (!isInsertPointRecordOK) {
            log.error("记录积分流水表失败，userId：{}，point：{}, source：{}, operation：{}", userId, point, source, operation);
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