package com.huiyu.service.core.service.business;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;

public interface IntegralRecordBusiness {

    boolean updateIntegral(Long userId, Integer integral, IntegralSourceRecordEnum source, IntegralOperationRecordEnum operation);
}
