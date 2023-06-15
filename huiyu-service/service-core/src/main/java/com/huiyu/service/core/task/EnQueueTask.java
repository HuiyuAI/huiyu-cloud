package com.huiyu.service.core.task;

import cn.hutool.json.JSONObject;
import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 11:26
 * @Description: api请求执行任务
 **/
@Data
@AllArgsConstructor
public class EnQueueTask implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private String url;
    private String body;
    private Long userId;
    private Long id;

    public EnQueueTask(Long id, Long userId, String url, String body) {
        this.url = url;
        this.body = body;
        this.userId = userId;
        this.id = id;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject(getBody());
        String url = getUrl();
        String body = getBody();
        Long taskId = getId();
        log.info("{} user: {}, url: {}, body: {}", "开始执行", userId, url, body);
        // todo 根据请求地址和请求参数调用api
        log.info("{} user: {}, url: {}, body: {}", "执行完成", userId, url, body);

        // 查询数据库
        TaskService taskService = SpringContext.getBean(TaskService.class);
        if (taskId != null && taskId != 0) {
            // 根据标识更新数据库状态
            taskService.update(
                    com.huiyu.service.core.entity.Task.builder()
                            .id(taskId)
                            .status(TaskStatusEnum.EXECUTED)
                            .updateTime(LocalDateTime.now())
                            .build()
            );
        }
        List<Task> taskList = taskService.getByStatus(0, 1);
        Executor messageProcessorExecutor = (Executor) SpringContext.getBean("messageProcessorExecutor");
        for (Task task : taskList) {
            task.setStatus(TaskStatusEnum.IN_QUEUE);
            taskService.update(task);
            messageProcessorExecutor.execute(new EnQueueTask(task.getId(), task.getUserId(), task.getUrl(), task.getBody()));
        }
    }
}
