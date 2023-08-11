package com.huiyu.service.core.model.vo;

import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-08-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointRecordPageVo {
    /**
     * 积分
     */
    private Integer num;
    /**
     * 增加为1，减少为0
     */
    private PointOperationTypeEnum operationType;
    /**
     * 操作来源
     */
    private PointOperationSourceEnum operationSource;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
