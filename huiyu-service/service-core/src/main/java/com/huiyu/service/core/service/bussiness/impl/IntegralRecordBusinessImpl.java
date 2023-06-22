package com.huiyu.service.core.service.bussiness.impl;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.entity.IntegralRecord;
import com.huiyu.service.core.service.IntegralRecordService;
import com.huiyu.service.core.service.auth.UserService;
import com.huiyu.service.core.service.bussiness.IntegralRecordBussiness;
import com.huiyu.service.core.utils.IdUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class IntegralRecordBusinessImpl implements IntegralRecordBussiness {

    @Resource
    private UserService userService;

    @Resource
    private IntegralRecordService integralRecordService;


    /**
     * 更新积分
     *
     * @param userId    更新的用户id
     * @param integral  需要操作的积分数值
     * @param source    操作来源
     * @param operation 积分增减
     * @return 是否更新成功
     */
    @Override
    public boolean updateIntegral(Long userId, Integer integral, IntegralSourceRecordEnum source, IntegralOperationRecordEnum operation) {
        if (integral == null || integral <= 0) {
            throw new IllegalArgumentException("积分数值不合法");
        }

        if (operation == IntegralOperationRecordEnum.REDUCE) {
            integral = -integral;
        }
        // todo 并发会导致其他线程操作失败，重试机制
        // 修改用户积分
        boolean update = userService.updateIntegralById(userId, integral);

        if (!update) {
            return false;
        }
        // 记录积分表
        IntegralRecord integralRecord = IntegralRecord.builder()
                .userId(String.valueOf(userId))
                .recordNo(IdUtils.getUuId())
                .fraction(integral)
                .operationType(operation)
                .operationSource(source)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDelete(0)
                .build();
        boolean isInsert = integralRecordService.insertRecord(integralRecord);
        if (!isInsert) {
            throw new RuntimeException("流水表插入失败");
        }

        return isInsert;
    }
}
