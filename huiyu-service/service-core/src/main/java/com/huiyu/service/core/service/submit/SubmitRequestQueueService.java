package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Cmd;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author wAnG
 * @Date 2023-06-12  23:30
 */
public class SubmitRequestQueueService<T extends Cmd> {

    @Resource
    @Qualifier("splitTaskExecutor")
    private Executor splitTaskExecutor;

    @Resource
    private ImageTaskService imageTaskService;

    // todo 需要把Cmd封装成task
    void submitToSplit(T t){
        Task task = convertTask(t);
        CompletableFuture.runAsync(() -> {
            imageTaskService.trySplitTask(task);
        },splitTaskExecutor);
    }

    private Task convertTask(T t){

        return null;
    }


}
