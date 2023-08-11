package com.huiyu.service.core.config.executor;

import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.config.TaskContext;
import com.huiyu.service.core.enums.PointOperationTypeEnum;
import com.huiyu.service.core.enums.PointOperationSourceEnum;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.enums.TaskStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.TaskService;
import com.huiyu.service.core.service.business.PointBusiness;
import com.huiyu.service.core.sd.submit.ImageTaskInvoker;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public
class GenerateThread implements Runnable {

    private final String ip;

    private final String source;

    private boolean stop = true;

    private final ImageTaskInvoker imageTaskInvoker;

    private final MonitorLinkedBlockingQueue<Byte> taskQueue;

    public GenerateThread(String ip, String source, MonitorLinkedBlockingQueue<Byte> taskQueue) {
        this.ip = ip;
        this.source = source;
        this.taskQueue = taskQueue;
        imageTaskInvoker = SpringContext.getBean(ImageTaskInvoker.class);
    }

    public void close() {
        this.stop = false;
    }

    @Override
    public void run() {
        while (stop) {
            try {
                taskQueue.take();
                Task nextTask = imageTaskInvoker.findNextTask(source);
                if (Objects.isNull(nextTask)) {
                    continue;
                }
                TaskContext.TASK_SUBMIT_CONTEXT.set(nextTask);
                imageTaskInvoker.invokerGenerate(nextTask, ip);
            } catch (InterruptedException e) {
                log.info("线程池销毁响应中断:ip:{},source:{}", ip, source);
                compensateHandle();
            } catch (Exception e) {
                log.error("invokerError:", e);
                compensateHandle();
            } finally {
                TaskContext.TASK_SUBMIT_CONTEXT.remove();
            }
        }
    }

    private void compensateHandle() {
        TaskService taskService = SpringContext.getBean(TaskService.class);
        PicService picService = SpringContext.getBean(PicService.class);
        PointBusiness pointBusiness = SpringContext.getBean(PointBusiness.class);
        // 重试次数上限
        Task task = TaskContext.TASK_SUBMIT_CONTEXT.get();
        if (task == null) {
            return;
        }
        if (false) {
            // todo 重试redis获取次数
//            task.setRetryCount(task.getRetryCount() + 1);
            taskQueue.offer(Byte.valueOf("1"));
        } else {
            // 回退积分
            pointBusiness.updatePoint(task.getUserId(), task.getPoint(), PointOperationSourceEnum.BACK, PointOperationTypeEnum.ADD, task.getRequestUuid());

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
