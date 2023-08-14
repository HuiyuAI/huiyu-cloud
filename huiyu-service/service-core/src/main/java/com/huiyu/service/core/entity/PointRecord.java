package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PointTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分表(PointRecord)实体类
 *
 * @author Naccl
 * @date 2023-06-09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("point_record")
public class PointRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    /**
     * 请求uuid
     */
    @TableField("request_uuid")
    private String requestUuid;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 增减的每日积分
     */
    @TableField("dailyPoint")
    private Integer dailyPoint;
    /**
     * 增减的永久积分
     */
    @TableField("point")
    private Integer point;
    /**
     * 增加为1，减少为0
     */
    @TableField("operation_type")
    private PointOperationTypeEnum operationType;
    /**
     * 操作来源
     */
    @TableField("operation_source")
    private PointOperationSourceEnum operationSource;
    /**
     * 积分类型
     */
    @TableField("point_type")
    private PointTypeEnum pointType;
    /**
     * 返还的每日积分
     */
    @TableField("return_daily_point")
    private Integer returnDailyPoint;
    /**
     * 返还的永久积分
     */
    @TableField("return_point")
    private Integer returnPoint;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;
}
