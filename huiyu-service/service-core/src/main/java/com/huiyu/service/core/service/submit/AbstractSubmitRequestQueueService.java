package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Cmd;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author wAnG
 * @Date 2023-06-12  23:30
 */
public abstract class AbstractSubmitRequestQueueService<T extends Cmd> {

    @Resource
    @Qualifier("splitTaskExecutor")
    private Executor splitTaskExecutor;

    @Resource
    private ImageTaskService imageTaskService;

    // todo 需要把Cmd封装成task
    public void submitToSplit(T t) {
        Task task = convertTask(t);
        CompletableFuture.runAsync(() -> {
            imageTaskService.trySplitTask(task);
        }, splitTaskExecutor);
    }

    public abstract Task convertTask(T t);

    public boolean isSupport(T t) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        String name = t.getClass().getName();
        return StringUtils.equals(typeName, name);
    }


}
