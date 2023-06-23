package com.huiyu.service.core.Hconfig;

import com.huiyu.service.core.Hconfig.annotation.HConfig;
import com.huiyu.service.core.config.executor.MonitorThreadPoolTaskExecutor;
import com.huiyu.service.core.config.executor.TaskExecutionRejectedHandler;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.service.submit.ImageTaskService;
import lombok.Data;
import org.slf4j.MDC;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.huiyu.service.core.config.executor.ExecutorConfig.TRACE_ID;


/**
 * @author wAnG
 * @Date 2023-06-24  01:47
 */

@HConfig(dataId = "huiyu-service-example.yaml")
public class AIExampleConfig implements HConfigOnChange<List<AIExampleConfig.ExampleItem>> {

    @Resource
    private ImageTaskService imageTaskService;

    private List<ExampleItem> exampleItems;

    @Override
    public void onChange(List<ExampleItem> exampleItem) {
        this.exampleItems = exampleItem;
        changeExecutor();
    }

    private void changeExecutor() {
        List<ThreadPoolExecutorDecorator> submitRequestExecutorList = imageTaskService.getSubmitRequestExecutorList();
        List<String> executorMarkList = submitRequestExecutorList.stream()
                .map(executor -> executor.getIp() + executor.getSourceName())
                .collect(Collectors.toList());
        List<ExampleItem> addConfig = exampleItems.stream()
                .filter(exampleItem -> !executorMarkList.contains(exampleItem.getIp() + exampleItem.getSource()))
                .collect(Collectors.toList());
        List<ThreadPoolExecutorDecorator> newExecutorList = addConfig.stream()
                .map(config -> creatExecutor(config.getIp(), config.getSource()))
                .collect(Collectors.toList());
        newExecutorList.addAll(submitRequestExecutorList);
        imageTaskService.setSubmitRequestExecutorList(newExecutorList);
    }

    public ThreadPoolExecutorDecorator creatExecutor(String ip, String source) {
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
                .sourceName(source)
                .ip(ip)
                .build();
    }

    @Data
    public static class ExampleItem {

        private String ip;

        private String source;

    }
}
