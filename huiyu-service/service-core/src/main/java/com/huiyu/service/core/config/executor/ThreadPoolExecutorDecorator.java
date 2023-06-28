package com.huiyu.service.core.config.executor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.Builder;
import lombok.Data;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author wAnG
 * @Date 2023-06-13  00:24
 */
@Data
@Builder
public class ThreadPoolExecutorDecorator {

    private final Executor threadPoolExecutor;

    private final String sourceName;

    private final String ip;

    private GenerateThread generateThread;

    private MonitorLinkedBlockingQueue<Byte> taskQueue;

    private final String monitorName;

    public void start() {
        taskQueue = new MonitorLinkedBlockingQueue<>(monitorName);
        generateThread = new GenerateThread(ip, sourceName, taskQueue);
        threadPoolExecutor.execute(generateThread);
    }

    public void shutdown() {
        generateThread.close();
        ((ThreadPoolTaskExecutor) TtlExecutors.unwrap(threadPoolExecutor)).shutdown();
    }

    public void commit() {
        taskQueue.offer(Byte.valueOf("1"));
    }

    public String getUniqueKey() {
        return ip + sourceName;
    }


}
