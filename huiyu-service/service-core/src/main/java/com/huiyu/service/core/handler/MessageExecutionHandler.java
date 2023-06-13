package com.huiyu.service.core.handler;

import com.huiyu.service.core.business.TaskBusiness;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    private TaskBusiness taskBusiness;

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof Task) {
            Task task = (Task) r;
            Long taskId = task.getId();
            if (taskId != null && taskId != 0) {
                taskBusiness.update(
                        Task.builder()
                                .id(taskId)
                                .status(TaskStatusEnum.UNEXECUTED)
                                .build()
                );
                return;
            }
            log.info("执行拒绝策略 : user: {}, url: {}, body: {}", task.getUrl(), task.getUrl(), task.getBody());
            taskBusiness.insertTask(
                    Task.builder()
                            .body(task.getBody())
                            .isDelete(0)
                            .url(task.getUrl())
                            .userId(task.getUserId())
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .status(TaskStatusEnum.UNEXECUTED)
                            .build()
            );
        }
    }
}