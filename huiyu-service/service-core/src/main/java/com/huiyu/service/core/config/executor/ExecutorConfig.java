package com.huiyu.service.core.config.executor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: 陈瑾
 * @date: 2023/6/12 8:51
 * @Description: 线程池配置
 **/
@Configuration
@EnableAsync
public class ExecutorConfig {

    public static final String TRACE_ID = "TraceId";

    @Bean(name = "submitRequestExecutor")
    public ThreadPoolExecutorDecorator submitRequestExecutor() {
        MonitorThreadPoolTaskExecutor executor = new MonitorThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(5);
        executor.setThreadNamePrefix("SUBMIT");
        executor.setMonitorName("submitRequestExecutor_test1");
        executor.setRejectedExecutionHandler(new TaskExecutionRejectedHandler());
        executor.setTaskDecorator(taskDecorator());
        executor.initialize();
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(executor)
                .sourceName("local")
                .build();
    }

    @Bean(name = "splitTaskExecutor")
    public ThreadPoolExecutorDecorator splitTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(2000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setTaskDecorator(taskDecorator());
        executor.initialize();
        Executor ttlExecutor = TtlExecutors.getTtlExecutor(executor);
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(ttlExecutor)
                .sourceName("split")
                .build();
    }

    @Bean(name = "translateExecutor")
    public ThreadPoolExecutorDecorator translateExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(2000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator(taskDecorator());
        executor.initialize();
        Executor ttlExecutor = TtlExecutors.getTtlExecutor(executor);
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(ttlExecutor)
                .sourceName("translate")
                .build();
    }

    private TaskDecorator taskDecorator() {
        return runnable -> {
            String traceId = MDC.get(TRACE_ID);
            return () -> {
                MDC.put(TRACE_ID, traceId);
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        };
    }
}
