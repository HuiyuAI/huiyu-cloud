package com.huiyu.service.core.handler;

import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 14:45
 * @Description: 自定义拒绝策略
 **/
@Slf4j
public class MessageExecutionHandler implements RejectedExecutionHandler {

    @Resource
    private TaskService taskService;

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof Task) {
            Task task = (Task) r;
            log.info("执行拒绝策略 : user: {}, url: {}, body: {}", task.getUrl(), task.getUrl(), task.getBody());
            String taskId = task.getId();
            if (StringUtils.isNotBlank(taskId)) {
                taskService.update(
                        Task.builder()
                                .id(taskId)
                                .status(TaskStatusEnum.UN_EXECUTED)
                                .build()
                );
                return;
            }
            task.setStatus(TaskStatusEnum.UN_EXECUTED);
            taskService.insertTask(task);
        }
    }
}