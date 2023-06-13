package com.huiyu.service.core.executor;

import com.huiyu.service.core.handler.MessageExecutionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));
        executor.setRejectedExecutionHandler(new MessageExecutionHandler());
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(executor)
                .sourceName("local")
                .build();
    }

    @Bean(name = "splitTaskExecutor")
    public Executor splitTaskExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));
        executor.setRejectedExecutionHandler(new MessageExecutionHandler());
        return executor;
    }
}
