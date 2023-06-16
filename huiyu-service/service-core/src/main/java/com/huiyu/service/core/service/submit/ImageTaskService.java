package com.huiyu.service.core.service.submit;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.executor.ThreadPoolExecutorDecorator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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


    public void trySplitTask(Task task) {
        List<Task> tasks = new ArrayList<>();

        if (judgeSplitTask(task)) {
            tasks.addAll(splitTask(task));
        } else {
            tasks.add(task);
        }

        Optional<ThreadPoolExecutorDecorator> executorOptional = submitRequestExecutorList.stream()
                .filter(decorator -> StringUtils.equals(task.getExecSource(), decorator.getSourceName()))
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


    // 判断是否需要拆分
    private boolean judgeSplitTask(Task task) {
        return task.getCount() > 1;
    }

    private List<Task> splitTask(Task task) {
        List<Task> taskList = Lists.newArrayList();
        if (task.getCount() > 1) {
            Integer count = task.getCount();
            task.setCount(1);
            for (int i = 0; i < count; i++) {
                Task copyTask = new Task();
                BeanUtil.copyProperties(task, copyTask);
                taskList.add(copyTask);
            }
        }
        return taskList;
    }

}
