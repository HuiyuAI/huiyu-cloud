package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.task.EnQueueTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 8:54
 * @Description: // TODO
 **/
@Service
public class MessageQueueService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Resource
    @Qualifier("messageProcessorExecutor")
    private Executor messageProcessorExecutor;

    @Async("messageQueueExecutor")
    public void enqueueMessage(Task task, int n) {
        if (task == null) {
            return;
        }
        for (int i = 0; i < n; i ++) {
            log.info("{} user: {}, url: {}, body: {}", "进入工作队列", task.getUserId(), task.getUrl(), task.getBody());
            messageProcessorExecutor.execute(new EnQueueTask(task.getId(), task.getUserId(), task.getUrl(), task.getBody()));
        }
    }
}
