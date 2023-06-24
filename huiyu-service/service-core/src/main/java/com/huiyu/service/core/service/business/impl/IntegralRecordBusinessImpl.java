package com.huiyu.service.core.service.business.impl;

import cn.hutool.core.util.IdUtil;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.entity.IntegralRecord;
import com.huiyu.service.core.service.IntegralRecordService;
import com.huiyu.service.core.service.auth.UserService;
import com.huiyu.service.core.service.business.IntegralRecordBusiness;
import com.huiyu.service.core.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class IntegralRecordBusinessImpl implements IntegralRecordBusiness {

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateIntegral(Long userId, Integer integral, IntegralSourceRecordEnum source, IntegralOperationRecordEnum operation) {
        if (integral == null || integral <= 0) {
            throw new IllegalArgumentException("积分数值不合法");
        }

        if (operation == IntegralOperationRecordEnum.REDUCE) {
            integral = -integral;
        }

        // 修改用户积分
        boolean isUpdateIntegralOK = userService.updateIntegralById(userId, integral);
        if (!isUpdateIntegralOK) {
            log.error("更新用户积分失败，userId：{}，integral：{}, source：{}, operation：{}", userId, integral, source, operation);
            throw new RuntimeException("异常错误");
        }

        // 关联请求uuid
        String requestUuid = RequestContext.REQUEST_UUID_CONTEXT.get();
        if (StringUtils.isEmpty(requestUuid)) {
            requestUuid = IdUtil.fastUUID();
        }

        // 记录积分表
        LocalDateTime now = LocalDateTime.now();
        IntegralRecord integralRecord = IntegralRecord.builder()
                .id(IdUtils.nextSnowflakeId())
                .userId(userId)
                .requestUuid(requestUuid)
                .num(Math.abs(integral))
                .operationType(operation)
                .operationSource(source)
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
        boolean isInsertIntegralRecordOK = integralRecordService.insertRecord(integralRecord);
        if (!isInsertIntegralRecordOK) {
            log.error("记录积分流水表失败，userId：{}，integral：{}, source：{}, operation：{}", userId, integral, source, operation);
            throw new RuntimeException("异常错误");
        }

        return true;
    }
}
