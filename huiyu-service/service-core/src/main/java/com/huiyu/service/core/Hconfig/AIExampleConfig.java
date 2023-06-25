package com.huiyu.service.core.Hconfig;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.huiyu.service.core.Hconfig.annotation.HConfig;
import com.huiyu.service.core.config.executor.MonitorThreadPoolTaskExecutor;
import com.huiyu.service.core.config.executor.TaskExecutionRejectedHandler;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.service.submit.ImageTaskService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.huiyu.service.core.config.TaskContext.INVOKER_URL_CONTEXT;
import static com.huiyu.service.core.config.executor.ExecutorConfig.TRACE_ID;


/**
 * @author wAnG
 * @Date 2023-06-24  01:47
 */

@Slf4j
@HConfig(dataId = "huiyu-service-example.json")
public class AIExampleConfig implements HConfigOnChange<AIExampleConfig.ChangeData> {

    @Resource
    private ImageTaskService imageTaskService;

    private List<ExampleItem> exampleItems;

    @Override
    public void onChange(ChangeData data) {
        this.exampleItems = data.getData();
        changeExecutor();
    }

    private void changeExecutor() {
        List<ThreadPoolExecutorDecorator> submitRequestExecutorList = imageTaskService.getSubmitRequestExecutorList();
        List<String> executorMarkList = submitRequestExecutorList.stream()
                .map(executor -> executor.getIp() + executor.getSourceName())
                .collect(Collectors.toList());
        List<String> exampleItemsConfigList = exampleItems.stream()
                .map(exampleItem -> exampleItem.getIp() + exampleItem.getSource())
                .collect(Collectors.toList());

        // 计算增加的配置
        List<ExampleItem> addConfig = exampleItems.stream()
                .filter(exampleItem -> !executorMarkList.contains(exampleItem.getIp() + exampleItem.getSource()))
                .collect(Collectors.toList());
        List<ThreadPoolExecutorDecorator> newExecutorList = addConfig.stream()
                .map(config -> creatExecutor(config.getIp(), config.getSource()))
                .collect(Collectors.toList());

        // 计算减少的配置
        submitRequestExecutorList = submitRequestExecutorList.stream()
                .filter(submitRequestExecutor -> {
                    if (!exampleItemsConfigList.contains(submitRequestExecutor.getIp() + submitRequestExecutor.getSourceName())) {
                        ((ThreadPoolTaskExecutor) TtlExecutors.unwrap(submitRequestExecutor.getThreadPoolExecutor())).shutdown();
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
        newExecutorList.addAll(submitRequestExecutorList);
        imageTaskService.setSubmitRequestExecutorList(newExecutorList);
    }

    public ThreadPoolExecutorDecorator creatExecutor(String ip, String source) {
        log.info("增加AI实例ip: {}, source: {}", ip, source);
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
                INVOKER_URL_CONTEXT.set(ip);
                MDC.put(TRACE_ID, traceId);
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                    INVOKER_URL_CONTEXT.remove();
                }
            };
        });
        executor.initialize();
        Executor ttlExecutor = TtlExecutors.getTtlExecutor(executor);
        return ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(ttlExecutor)
                .sourceName(source)
                .ip(ip)
                .build();
    }

    public List<ExampleItem> getExampleItems() {
        return exampleItems;
    }

    @Data
    public static class ExampleItem {

        private String ip;

        private String source;

    }

    @Data
    public static class ChangeData {
        List<ExampleItem> data;
    }
}
