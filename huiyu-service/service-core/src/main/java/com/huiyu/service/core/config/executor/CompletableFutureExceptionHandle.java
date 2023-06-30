package com.huiyu.service.core.config.executor;

import com.google.common.collect.Lists;
import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.config.TaskContext;
import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.constant.PicStatusEnum;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.TaskService;
import com.huiyu.service.core.service.business.IntegralRecordBusiness;
import com.huiyu.service.core.service.submit.ImageTaskService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.function.Function;

import static com.huiyu.service.core.config.TaskContext.TASK_INFO_CONTEXT;

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
