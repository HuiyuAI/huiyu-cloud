package com.huiyu.service.core.service.submit;

import com.google.common.collect.Lists;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.executor.ThreadPoolExecutorDecorator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
    private List<AbstractImageTaskInvoker> imageTaskInvokerList;

    public void trySplitTask(Task task){
        List<Task> tasks = new ArrayList<>();

        if (judgeSplitTask()){
            tasks.addAll(splitTask());
        } else {
            tasks.add(task);
        }

        Optional<ThreadPoolExecutorDecorator> executorOptional = submitRequestExecutorList.stream()
                .filter(decorator -> StringUtils.equals(task.getExecSource(), decorator.getSourceName()))
                .findFirst();

        if(!executorOptional.isPresent()){
            log.error("未分配执行源");
            return;
        }

        AbstractImageTaskInvoker imageTaskInvoker = imageTaskInvokerList.stream()
                .filter(imageTaskInvokerItem -> imageTaskInvokerItem.isSupper(task))
                .findFirst()
                .orElse(null);

        if(Objects.isNull(imageTaskInvoker)){
            log.error("未分配执行器");
            return;
        }

        executorOptional.ifPresent(executor -> {
            tasks.forEach(taskItem -> {
                CompletableFuture.runAsync(() -> {
                    imageTaskInvoker.invokerGenerate(taskItem);
                },executor.getThreadPoolExecutor());
            });
        });


    }


    // 判断是否需要拆分
    private boolean judgeSplitTask(){
        return false;
    }

    private List<Task> splitTask(){

        return Lists.newArrayList();
    }

}
