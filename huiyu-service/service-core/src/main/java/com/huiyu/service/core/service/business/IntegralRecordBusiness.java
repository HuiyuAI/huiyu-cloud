package com.huiyu.service.core.service.business;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.entity.Task;

public interface IntegralRecordBusiness {

    /**
     * 用户积分修改
     *
     * @param userId    用户id
     * @param integral  积分值
     * @param source    积分来源
     * @param operation 积分修改方式
     * @param task      任务
     * @return
     */
    boolean updateIntegral(Long userId, Integer integral, IntegralSourceRecordEnum source, IntegralOperationRecordEnum operation, Task task);
}
