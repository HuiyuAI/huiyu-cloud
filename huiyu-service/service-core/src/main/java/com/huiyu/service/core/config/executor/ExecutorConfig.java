package com.huiyu.service.core.config.executor;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
        executor.setTaskDecorator(runnable -> {
            String traceId = MDC.get(TRACE_ID);
            return () -> {
                MDC.put(TRACE_ID, traceId);
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        });
        executor.initialize();
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(executor)
                .sourceName("local")
                .build();
    }

    @Bean(name = "splitTaskExecutor")
    public ThreadPoolExecutorDecorator splitTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setTaskDecorator(runnable -> {
            String traceId = MDC.get(TRACE_ID);
            return () -> {
                MDC.put(TRACE_ID, traceId);
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        });
        executor.initialize();
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(executor)
                .sourceName("split")
                .build();
    }
}
