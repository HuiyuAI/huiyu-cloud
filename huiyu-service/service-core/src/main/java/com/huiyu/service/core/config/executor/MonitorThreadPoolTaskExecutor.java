package com.huiyu.service.core.config.executor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author wAnG
 * @Date 2023-06-18  02:50
 */
public class MonitorThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private String monitorName;

    @Override
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        return (BlockingQueue) (queueCapacity > 0 ? new MonitorLinkedBlockingQueue<>(queueCapacity, monitorName) : new SynchronousQueue());
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }
}
