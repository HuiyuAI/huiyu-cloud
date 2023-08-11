package com.huiyu.service.core.service.business.impl;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.exception.BizException;
import com.huiyu.service.core.service.PointRecordService;
import com.huiyu.service.core.service.UserService;
import com.huiyu.service.core.service.business.UserBusiness;
import com.huiyu.service.core.utils.IdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
