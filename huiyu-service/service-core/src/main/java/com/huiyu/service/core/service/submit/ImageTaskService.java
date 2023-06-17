package com.huiyu.service.core.service.submit;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.huiyu.service.core.config.ImageTaskContext.TASK_INFO_CONTEXT;

/**
 * @author wAnG
 * @Date 2023-06-12  23:44
 */
@Service
@Slf4j
public class ImageTaskService {

    @Resource
    private List<ThreadPoolExecutorDecorator> submitRequestExecutorList;

    @Resource
    private ImageTaskInvoker imageTaskInvokerList;


    public void trySplitTask(Task task, Dto dto) {
        List<Task> tasks = splitTask(task, dto);

        execGenerate(tasks, task.getExecSource());
    }

    public void execGenerate(List<Task> tasks, String taskExecSource) {
        Optional<ThreadPoolExecutorDecorator> executorOptional = submitRequestExecutorList.stream()
                .filter(decorator -> StringUtils.equals(taskExecSource, decorator.getSourceName()))
                .findFirst();

        if (!executorOptional.isPresent()) {
            log.error("未分配执行源");
            return;
        }
        // todo 缺少多级队列
        executorOptional.ifPresent(executor -> {
            tasks.forEach(taskItem -> {
                TASK_INFO_CONTEXT.set(taskItem);
                CompletableFuture.runAsync(() -> {
                    imageTaskInvokerList.invokerGenerate(taskItem);
                }, executor.getThreadPoolExecutor());
                TASK_INFO_CONTEXT.remove();
            });
        });
    }

    private List<Task> splitTask(Task task, Dto dto) {
        List<Task> taskList = Lists.newArrayList();
        if (task.getNum() == 1) {
            dto.setResImageUuid(IdUtil.fastUUID());
            task.setBody(JacksonUtils.toJsonStr(dto));
            taskList.add(task);
            return taskList;
        }
        for (int i = 0; i < task.getNum(); i++) {
            Task copyTask = new Task();
            BeanUtil.copyProperties(task, copyTask);

            copyTask.setId(IdUtils.nextSnowflakeId());
            dto.setResImageUuid(IdUtil.fastUUID());
            copyTask.setBody(JacksonUtils.toJsonStr(dto));
            copyTask.setNum(1);
            taskList.add(copyTask);
        }
        return taskList;
    }

}
