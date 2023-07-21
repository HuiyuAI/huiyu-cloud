package com.huiyu.service.core.service.business;

import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.entity.Task;

public interface PointBusiness {

    /**
     * 用户积分修改
     *
     * @param userId    用户id
     * @param point     积分值
     * @param source    积分来源
     * @param operation 积分修改方式
     * @param task      任务
     * @return
     */
    boolean updatePoint(Long userId, Integer point, PointOperationSourceEnum source, PointOperationTypeEnum operation, Task task);
}
