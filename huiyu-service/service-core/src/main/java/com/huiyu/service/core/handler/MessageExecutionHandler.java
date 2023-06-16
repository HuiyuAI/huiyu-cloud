package com.huiyu.service.core.handler;

import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.huiyu.service.core.config.ImageTaskContext.TASK_INFO_CONTEXT;


/**
 * @author: 陈瑾
 * @date: 2023/6/12 14:45
 * @Description: 自定义拒绝策略
 **/
@Slf4j
public class MessageExecutionHandler implements RejectedExecutionHandler {


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        TaskService taskService = SpringContext.getBean(TaskService.class);
        try {
            Task task = TASK_INFO_CONTEXT.get();
            if (task != null) {
                log.info("执行拒绝策略 : user: {}, url: {}, body: {}", task.getUrl(), task.getUrl(), task.getBody());
                Long id = task.getId();
                if (id != null && id != 0) {
                    Task wrapper = Task.builder()
                            .taskId(task.getTaskId())
                            .status(TaskStatusEnum.UN_EXECUTED)
                            .build();
                    taskService.update(wrapper);
                    return;
                }
                taskService.insertTask(task);
            }
        } finally {
            TASK_INFO_CONTEXT.remove();
        }
    }
}
