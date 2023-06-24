package com.huiyu.service.core.config.executor;

import com.google.common.collect.Lists;
import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.config.TaskContext;
import com.huiyu.service.core.constant.PicStatusEnum;
import com.huiyu.service.core.constant.TaskStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.TaskService;
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
            // 补充处理
            compensateHandle();
        }
        TaskContext.TASK_SUBMIT_CONTEXT.remove();
        return null;
    };

    private static void compensateHandle() {
        TaskService taskService = SpringContext.getBean(TaskService.class);
        ImageTaskService imageTaskService = SpringContext.getBean(ImageTaskService.class);
        PicService picService = SpringContext.getBean(PicService.class);
        // 重试次数上限
        Task task = TaskContext.TASK_SUBMIT_CONTEXT.get();
        if (task == null) {
            return;
        }
        if (task.getRetryCount() < 3) {
            task.setRetryCount(task.getRetryCount() + 1);
            task.setStatus(TaskStatusEnum.UN_EXECUTED);
            imageTaskService.execGenerate(Lists.newArrayList(task), task.getExecSource());
        } else {
            // todo 回退消耗(积分或者卷)
            Task TaskDO = Task.builder()
                    .id(task.getId())
                    .status(TaskStatusEnum.DISCARD)
                    .updateTime(LocalDateTime.now())
                    .build();
            taskService.updateById(TaskDO);

            Long taskId = task.getId();
            Pic pic = picService.getByTaskId(taskId);
            pic.setStatus(PicStatusEnum.DISCARD);
            pic.setUpdateTime(LocalDateTime.now());
            picService.updateByUuid(pic);
        }

    }

}
