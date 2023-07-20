package com.huiyu.service.core.model.query;

import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.enums.TaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-07-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskQuery {

    /**
     * 任务Id
     */
    private Long id;
    /**
     * 请求uuid
     */
    private String requestUuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务类型
     */
    private TaskTypeEnum type;
    /**
     * 任务状态
     */
    private TaskStatusEnum status;
    /**
     * 执行源
     */
    private String execSource;
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
}
