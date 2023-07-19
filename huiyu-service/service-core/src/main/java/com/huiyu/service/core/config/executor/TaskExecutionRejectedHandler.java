package com.huiyu.service.core.config.executor;

import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.huiyu.service.core.config.TaskContext.TASK_INFO_CONTEXT;


/**
 * @author: 陈瑾
 * @date: 2023/6/12 14:45
 * @Description: 自定义拒绝策略
 **/
@Slf4j
public class TaskExecutionRejectedHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        TaskService taskService = SpringContext.getBean(TaskService.class);
        try {
            Task task = TASK_INFO_CONTEXT.get();
            Long taskId = task.getId();
            log.info("执行拒绝策略: taskId: {}, userId: {}, type: {}, execSource: {}, createTime: {}", task.getId(), task.getUserId(), task.getType().getDictKey(), task.getExecSource(), task.getCreateTime());
            if (taskService.getById(taskId) != null) {
                Task wrapper = Task.builder()
                        .id(taskId)
                        .status(TaskStatusEnum.UN_EXECUTED)
                        .build();
                taskService.updateById(wrapper);
            }
        } finally {
            TASK_INFO_CONTEXT.remove();
        }
    }
}
