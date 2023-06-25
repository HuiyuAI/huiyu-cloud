package com.huiyu.service.core.service.submit;

import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.utils.NewPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import static com.huiyu.service.core.config.executor.CompletableFutureExceptionHandle.ExceptionLogHandle;

/**
 * @author wAnG
 * @Date 2023-06-12  23:30
 */
@Slf4j
public abstract class AbstractSubmitRequestQueueService<T extends Cmd> {

    @Resource
    @Qualifier("splitTaskExecutor")
    private ThreadPoolExecutorDecorator splitTaskExecutor;

    @Resource
    private ImageTaskService imageTaskService;

    public void submitToSplit(T t) {
        NewPair<Task, Dto> taskDtoPair = convertTask(t);
        String execSource = chooseExecSource(t);
        Task task = taskDtoPair.getKey();
        task.setExecSource(execSource);
        Dto dto = taskDtoPair.getValue();
        CompletableFuture.runAsync(() -> imageTaskService.trySplitTask(task, dto), splitTaskExecutor.getThreadPoolExecutor())
                .exceptionally(ExceptionLogHandle);
    }

    public boolean isSupport(T t) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        String name = t.getClass().getName();
        return StringUtils.equals(typeName, name);
    }

    public abstract NewPair<Task, Dto> convertTask(T t);

    public abstract String chooseExecSource(T t);


}
