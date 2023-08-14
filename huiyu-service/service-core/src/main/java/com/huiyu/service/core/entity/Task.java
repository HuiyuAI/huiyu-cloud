package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huiyu.service.core.enums.PointTypeEnum;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("task")
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务Id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 请求uuid
     */
    @TableField("request_uuid")
    private String requestUuid;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 任务类型
     */
    @TableField("type")
    private TaskTypeEnum type;
    /**
     * SD DTO
     */
    @TableField("body")
    private String body;
    /**
     * 任务状态
     */
    @TableField("status")
    private TaskStatusEnum status;
    /**
     * 消耗积分
     */
    @TableField("point")
    private Integer point;
    /**
     * 执行源
     */
    @TableField("exec_source")
    private String execSource;
    /**
     * 积分类型
     */
    @TableField("point_type")
    private PointTypeEnum pointType;
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

    /**
     * 任务数
     */
    @JsonIgnore
    private transient Integer num;
    /**
     * 重试次数
     */
    @JsonIgnore
    private transient Integer retryCount;
}
