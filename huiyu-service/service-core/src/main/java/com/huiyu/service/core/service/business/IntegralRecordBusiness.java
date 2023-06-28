package com.huiyu.service.core.service.business;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;

public interface IntegralRecordBusiness {

    /**
     * 用户积分修改
     *
     * @param userId    用户id
     * @param integral  积分值
     * @param source    积分来源
     * @param operation 积分修改方式
     * @return
     */
    boolean updateIntegral(Long userId, Integer integral, IntegralSourceRecordEnum source, IntegralOperationRecordEnum operation);
}
