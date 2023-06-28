package com.huiyu.service.core.config.executor;

import com.huiyu.service.core.config.SpringContext;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.service.submit.ImageTaskInvoker;
import lombok.extern.slf4j.Slf4j;

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
                imageTaskInvoker.invokerGenerate(nextTask, ip);
            } catch (InterruptedException e) {
                log.info("线程池销毁响应中断:ip:{},source:{}", ip, source);
            } catch (Exception e) {
                log.error("invokerError:", e);
            }
        }
    }
}