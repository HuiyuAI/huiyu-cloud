package com.huiyu.service.core.entity;

import com.huiyu.service.core.enums.IntegralOperationRecordEnum;
import com.huiyu.service.core.enums.IntegralSourceRecordEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分表(IntegralRecord)实体类
 *
 * @author Naccl
 * @date 2023-06-09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegralRecord implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * taskID
     */
    private Long taskId;
    /**
     * 积分
     */
    private Integer num;
    /**
     * 增加为1，减少为0
     */
    private IntegralOperationRecordEnum operationType;
    /**
     * 操作来源
     */
    private IntegralSourceRecordEnum operationSource;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    private Integer isDelete;
}
