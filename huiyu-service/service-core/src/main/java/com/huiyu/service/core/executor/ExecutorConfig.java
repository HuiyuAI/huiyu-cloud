package com.huiyu.service.core.executor;

import com.huiyu.service.core.handler.MessageExecutionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
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

    @Bean(name = "submitRequestExecutor")
    public ThreadPoolExecutorDecorator submitRequestExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(5);
        executor.setRejectedExecutionHandler(new MessageExecutionHandler());
        executor.initialize();
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(executor)
                .sourceName("local")
                .build();
    }

    @Bean(name = "splitTaskExecutor")
    public TaskExecutor splitTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
