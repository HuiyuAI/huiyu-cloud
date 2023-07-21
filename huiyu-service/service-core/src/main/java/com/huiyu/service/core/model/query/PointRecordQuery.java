package com.huiyu.service.core.model.query;

import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-07-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointRecordQuery {
    private Long id;
    /**
     * 请求uuid
     */
    private String requestUuid;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 增加为1，减少为0
     */
    private PointOperationTypeEnum operationType;
    /**
     * 操作来源
     */
    private PointOperationSourceEnum operationSource;
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
}
