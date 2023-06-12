package com.huiyu.service.core.handler;

import com.huiyu.service.core.business.TaskBusiness;
import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.task.EnQueueTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 14:45
 * @Description: 自定义拒绝策略
 **/
public class MessageExecutionHandler implements RejectedExecutionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        TaskBusiness taskBussiness = SpringContext.getBean(TaskBusiness.class);
        if (r instanceof EnQueueTask) {
            EnQueueTask task = (EnQueueTask) r;
            Long taskId = task.getId();
            if (taskId != null && taskId != 0) {
                taskBussiness.update(
                        Task.builder()
                                .id(taskId)
                                .status(0)
                                .build()
                );
                return;
            }
            log.info("{} : user: {}, url: {}, body: {}", "执行拒绝策略", task.getUrl(), task.getUrl(), task.getBody());
            taskBussiness.insertTask(
                    Task.builder()
                            .body(task.getBody())
                            .isDelete(0)
                            .url(task.getUrl())
                            .userId(task.getUserId())
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .status(0)
                            .build()
            );
        }
    }
}