package com.huiyu.service.core.model.bo;

import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-08-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePointHandlerBO {
    /**
     * 计算后增减的每日积分
     */
    private Integer targetDailyPointDiff;
    /**
     * 计算后增减的永久积分
     */
    private Integer targetPointDiff;
    /**
     * 计算后的积分增减类型
     */
    private PointOperationTypeEnum operationType;
    /**
     * 计算后的积分类型
     */
    private PointTypeEnum pointType;
}
