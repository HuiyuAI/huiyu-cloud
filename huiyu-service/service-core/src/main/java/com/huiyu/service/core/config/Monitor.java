package com.huiyu.service.core.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wAnG
 * @Date 2023-06-16  22:02
 * 不要在Spring初始化的时候使用这个类
 */
@Component
@DependsOn("springContext")
public class Monitor implements InitializingBean {

    private static CollectorRegistry collectorRegistry;

    private static final Map<String, Counter> counterNameMap = new HashMap<>();

    private static final Map<String, Summary> summaryNameMap = new HashMap<>();


    public static void recordOne(String monitorName) {
        Counter monitor = exitsCounter(monitorName);
        if (Objects.isNull(monitor)) {
            monitor = Counter.build().name(monitorName).help(monitorName).register(collectorRegistry);
            counterNameMap.put(monitorName, monitor);
        }
        monitor.inc();
    }

    public static void recordTime(String monitorName, long time) {
        Summary summary = exitsSummary(monitorName);
        if (Objects.isNull(summary)) {
            summary = Summary.build().name(monitorName).help(monitorName).register(collectorRegistry);
            summaryNameMap.put(monitorName, summary);
        }
        summary.observe(time);
    }


    private static Counter exitsCounter(String monitorName) {
        return counterNameMap.get(monitorName);
    }

    private static Summary exitsSummary(String monitorName) {
        return summaryNameMap.get(monitorName);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Monitor.collectorRegistry = SpringContext.getBean(CollectorRegistry.class);
    }
}
