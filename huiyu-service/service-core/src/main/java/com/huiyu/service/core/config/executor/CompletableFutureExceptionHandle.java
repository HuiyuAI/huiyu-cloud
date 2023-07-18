package com.huiyu.service.core.config.executor;

import com.huiyu.service.core.config.TaskContext;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * @author wAnG
 * @Date 2023-06-18  21:45
 */
@Slf4j
public class CompletableFutureExceptionHandle {

    public static Function<Throwable, Void> ExceptionLogHandle = (e) -> {
        if (e != null) {
            Exception exception = new Exception(e);
            log.error("异步流程出错", exception);
        }
        TaskContext.TASK_SUBMIT_CONTEXT.remove();
        return null;
    };

}
