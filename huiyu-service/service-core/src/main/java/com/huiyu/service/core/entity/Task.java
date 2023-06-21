package com.huiyu.service.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.constant.TaskTypeEnum;
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
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务Id
     */
    private Long taskId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务类型
     */
    private TaskTypeEnum type;
    /**
     * SD DTO
     */
    private String body;
    /**
     * 任务状态
     */
    private TaskStatusEnum status;
    /**
     * 执行源
     */
    private String execSource;
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

    /**
     * 任务数
     */
    @JsonIgnore
    private transient Integer num;
}
