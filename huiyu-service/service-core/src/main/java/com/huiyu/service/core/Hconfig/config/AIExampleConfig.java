package com.huiyu.service.core.Hconfig.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.huiyu.service.core.Hconfig.base.HConfigOnChange;
import com.huiyu.service.core.Hconfig.base.HConfigType;
import com.huiyu.service.core.Hconfig.base.annotation.HConfig;
import com.huiyu.service.core.config.executor.MonitorLinkedBlockingQueue;
import com.huiyu.service.core.config.executor.MonitorThreadPoolTaskExecutor;
import com.huiyu.service.core.config.executor.TaskExecutionRejectedHandler;
import com.huiyu.service.core.config.executor.ThreadPoolExecutorDecorator;
import com.huiyu.service.core.service.impl.TaskServiceImpl;
import com.huiyu.service.core.service.submit.ImageTaskService;
import com.huiyu.service.core.service.submit.chooseStrategy.ExecChooseContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.huiyu.service.core.config.TaskContext.INVOKER_URL_CONTEXT;
import static com.huiyu.service.core.config.executor.ExecutorConfig.TRACE_ID;


/**
 * @author wAnG
 * @Date 2023-06-24  01:47
 */

@Slf4j
@HConfig(dataId = "example", suffix = HConfigType.JSON)
@DependsOn("springContext")
public class AIExampleConfig implements HConfigOnChange<AIExampleConfig.ChangeData> {

    @Resource
    private ImageTaskService imageTaskService;

    @Resource
    private TaskServiceImpl taskService;

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

        Map<Object, ExampleItem> newExampleItemsConfigMap = newExampleItems.stream()
                .collect(Collectors.toMap(exampleItem -> exampleItem.getIp() + exampleItem.getSource(), exampleItem -> exampleItem));

        Map<Object, ExampleItem> oldExampleItemsConfigMap = exampleItems.stream()
                .collect(Collectors.toMap(exampleItem -> exampleItem.getIp() + exampleItem.getSource(), exampleItem -> exampleItem));

        // 计算减少的配置
        submitRequestExecutorList
                .forEach(submitRequestExecutor -> {
                    String mapKey = submitRequestExecutor.getUniqueKey();
                    if (Objects.isNull(newExampleItemsConfigMap.get(mapKey))) {
                        ExampleItem exampleItem = oldExampleItemsConfigMap.get(mapKey);
                        synchronized (exampleItem) {
                            ExecChooseContext.examplePoint.remove(exampleItem);
                        }
                        reloadDecQueue(submitRequestExecutor.getTaskQueue(), submitRequestExecutor.getSourceName());
                        submitRequestExecutor.shutdown();
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
                .filter(submitRequestExecutor -> !Objects.isNull(exampleItemsConfigMap.get(submitRequestExecutor.getUniqueKey())))
                .collect(Collectors.toList());
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
        ThreadPoolExecutorDecorator executorDecorator = ThreadPoolExecutorDecorator.builder()
                .threadPoolExecutor(ttlExecutor)
                .sourceName(source)
                .monitorName("submitRequestExecutor_" + ip + "_" + source)
                .ip(ip)
                .build();
        executorDecorator.start();
        MonitorLinkedBlockingQueue<Byte> taskQueue = executorDecorator.getTaskQueue();
        reloadAddQueue(taskQueue, executorDecorator.getSourceName());
        return executorDecorator;
    }

    private void reloadAddQueue(MonitorLinkedBlockingQueue<Byte> taskQueue, String source) {
        synchronized (ExecChooseContext.submitQueueList) {
            ExecChooseContext.submitQueueList.put(source, taskQueue);
            long taskCount = ExecChooseContext.submitQueueList.values().stream()
                    .map(LinkedBlockingQueue::size)
                    .count();
            if (taskCount == 0) {
                return;
            }

            long queueSize = ExecChooseContext.submitQueueList.size();
            long avgTask = taskCount / queueSize;

            ExecChooseContext.submitQueueList.forEach((replaceSource, replaceQueue) -> {
                long count = replaceQueue.size() - avgTask;
                if (count < 0) {
                    count = 0;
                }
                taskService.batchUpdateBySource(source, replaceSource, count);
                for (int i = 0; i < count; i++) {
                    try {
                        taskQueue.offer(replaceQueue.poll(100, TimeUnit.MILLISECONDS));
                    } catch (InterruptedException e) {
                        log.error("reload_error", e);
                        break;
                    }
                }
            });
        }
    }

    private void reloadDecQueue(MonitorLinkedBlockingQueue<Byte> taskQueue, String source) {
        synchronized (ExecChooseContext.submitQueueList) {
            ExecChooseContext.submitQueueList.remove(source);
            int taskCount = taskQueue.size();
            long queueSize = ExecChooseContext.submitQueueList.size();

            long avgTask = taskCount / queueSize;

            ExecChooseContext.submitQueueList.forEach((targetSource, targetQueue) -> {
                taskService.batchUpdateBySource(source, targetSource, avgTask);
                for (int i = 0; i < avgTask; i++) {
                    try {
                        targetQueue.add(taskQueue.poll(100, TimeUnit.MILLISECONDS));
                    } catch (InterruptedException e) {
                        log.error("reload_error", e);
                        break;
                    }
                }
            });
        }
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
