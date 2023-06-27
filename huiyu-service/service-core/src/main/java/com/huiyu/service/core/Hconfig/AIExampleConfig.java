package com.huiyu.service.core.Hconfig;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.huiyu.service.core.Hconfig.annotation.HConfig;
import com.huiyu.service.core.config.executor.MonitorThreadPoolTaskExecutor;
import com.huiyu.service.core.config.executor.TaskExecutionRejectedHandler;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.service.submit.ImageTaskService;
import com.huiyu.service.core.service.submit.chooseStrategy.ExecChooseContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.huiyu.service.core.config.TaskContext.INVOKER_URL_CONTEXT;
import static com.huiyu.service.core.config.executor.ExecutorConfig.TRACE_ID;


/**
 * @author wAnG
 * @Date 2023-06-24  01:47
 */

@Slf4j
@HConfig(dataId = "example", suffix = HConfigType.JSON)
public class AIExampleConfig implements HConfigOnChange<AIExampleConfig.ChangeData> {

    @Resource
    private ImageTaskService imageTaskService;

    private List<ExampleItem> exampleItems = new ArrayList<>();

    @Override
    public void onChange(ChangeData data) {
        if (!CollectionUtils.isEmpty(exampleItems)) {
            removeExecutor(data.getData());
        }
        this.exampleItems = data.getData();
        changeExecutor();
    }

    private void removeExecutor(List<ExampleItem> newExampleItems) {
        List<ThreadPoolExecutorDecorator> submitRequestExecutorList = imageTaskService.getSubmitRequestExecutorList();

        Map<Object, ExampleItem> exampleItemsConfigMap = newExampleItems.stream()
                .collect(Collectors.toMap(exampleItem -> exampleItem.getIp() + exampleItem.getSource(), exampleItem -> exampleItem));

        // 计算减少的配置
        submitRequestExecutorList
                .forEach(submitRequestExecutor -> {
                    String mapKey = submitRequestExecutor.getIp() + submitRequestExecutor.getSourceName();
                    if (Objects.isNull(exampleItemsConfigMap.get(mapKey))) {
                        ExampleItem exampleItem = exampleItemsConfigMap.get(mapKey);
                        synchronized (exampleItem) {
                            ExecChooseContext.examplePoint.remove(exampleItem);
                        }
                        ((ThreadPoolTaskExecutor) TtlExecutors.unwrap(submitRequestExecutor.getThreadPoolExecutor())).shutdown();
                    }
                });
    }

    private void changeExecutor() {
        List<ThreadPoolExecutorDecorator> submitRequestExecutorList = imageTaskService.getSubmitRequestExecutorList();
        List<String> executorMarkList = submitRequestExecutorList.stream()
                .map(executor -> executor.getIp() + executor.getSourceName())
                .collect(Collectors.toList());
        Map<Object, ExampleItem> exampleItemsConfigMap = exampleItems.stream()
                .collect(Collectors.toMap(exampleItem -> exampleItem.getIp() + exampleItem.getSource(), exampleItem -> exampleItem));

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
                    if (Objects.isNull(exampleItemsConfigMap.get(submitRequestExecutor.getIp() + submitRequestExecutor.getSourceName()))) {
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
        executor.setQueueCapacity(1000);
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

        /**
         * 不同实例的执行效率
         */
        private Integer efficiency;

    }

    @Data
    public static class ChangeData {
        List<ExampleItem> data;
    }
}
