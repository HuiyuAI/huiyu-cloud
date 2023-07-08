package com.huiyu.service.core.config;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
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
@Slf4j
public class Monitor implements InitializingBean {

    private static CollectorRegistry collectorRegistry;

    private static Map<String, Collector> namesToCollectors;

    private static final Map<String, Counter> counterNameMap = new HashMap<>();

    private static final Map<String, Summary> summaryNameMap = new HashMap<>();

    private static final Map<String, Gauge> gaugeNameMap = new HashMap<>();

    public static void recordOne(String monitorName) {
        recordOne(monitorName, StringUtils.EMPTY);
    }

    public static void recordOne(String monitorName, String labelName) {
        try {
            monitorName = replaceName(monitorName);
            Counter counter = exitsCounter(monitorName, labelName);
            if (StringUtils.isNotBlank(labelName)) {
                counter.labels(labelName).inc();
                return;
            }
            counter.inc();
        } catch (Exception e) {
            log.error("监控异常", e);
        }
    }

    public static void recordTime(String monitorName, long time) {
        recordTime(monitorName, StringUtils.EMPTY, time);
    }

    public static void recordTime(String monitorName, String labelName, long time) {
        try {
            monitorName = replaceName(monitorName);
            Summary summary = exitsSummary(monitorName, labelName);
            if (Objects.isNull(summary)) {
                summary = Summary.build().name(monitorName).help(monitorName).register(collectorRegistry);
                summaryNameMap.put(monitorName, summary);
            }
            if (StringUtils.isNotBlank(labelName)) {
                summary.labels(labelName).observe(time);
                return;
            }
            summary.observe(time);
        } catch (Exception e) {
            log.error("监控异常", e);
        }
    }

    public static void recordInc(String monitorName) {
        recordInc(monitorName, StringUtils.EMPTY);
    }

    public static void recordInc(String monitorName, String labelName) {
        try {
            monitorName = replaceName(monitorName);
            Gauge gauge = exitsGauge(monitorName, replaceName(labelName));
            if (StringUtils.isNotBlank(labelName)) {
                gauge.labels(labelName).inc();
                return;
            }
            gauge.inc();
        } catch (Exception e) {
            log.error("监控异常", e);
        }
    }

    public static void recordDec(String monitorName, String labelName) {
        try {
            monitorName = replaceName(monitorName);
            Gauge gauge = exitsGauge(monitorName, replaceName(labelName));
            if (StringUtils.isNotBlank(labelName)) {
                gauge.labels(labelName).dec();
                return;
            }
            gauge.dec();
        } catch (Exception e) {
            log.error("监控异常", e);
        }
    }


    private static Counter exitsCounter(String monitorName, String labelName) {

        Counter counter = counterNameMap.get(monitorName);

        if (Objects.isNull(counter)) {
            Counter.Builder build = Counter.build().name(monitorName).help(monitorName);
            if (StringUtils.isNotBlank(labelName)) {
                build.labelNames(monitorName);
            }
            try {
                synchronized (counterNameMap) {
                    if (Objects.isNull(namesToCollectors.get(monitorName))) {
                        counter = build.register(collectorRegistry);
                    } else {
                        if (Objects.nonNull(counterNameMap.get(monitorName))) {
                            counter = counterNameMap.get(monitorName);
                        } else {
                            counter = build.create();
                            counterNameMap.put(monitorName, counter);
                        }
                        counter = build.create();
                    }
                }
            } catch (Exception e) {
                log.error("Monitor_register_error", e);
            }
            return counter;
        }

        return counter;
    }

    private static Summary exitsSummary(String monitorName, String labelName) {
        Summary summary = summaryNameMap.get(monitorName);

        if (Objects.isNull(summary)) {
            Summary.Builder build = Summary.build().name(monitorName).help(monitorName);
            if (StringUtils.isNotBlank(labelName)) {
                build.labelNames(monitorName);
            }
            try {
                synchronized (summaryNameMap) {
                    if (Objects.isNull(namesToCollectors.get(monitorName))) {
                        summary = build.register(collectorRegistry);
                        summaryNameMap.put(monitorName, summary);
                    } else {
                        if (Objects.nonNull(summaryNameMap.get(monitorName))) {
                            summary = summaryNameMap.get(monitorName);
                        } else {
                            summary = build.create();
                            summaryNameMap.put(monitorName, summary);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Monitor_register_error", e);
            }
            return summary;
        }

        return summary;
    }

    private static Gauge exitsGauge(String monitorName, String labelName) {
        Gauge gauge = gaugeNameMap.get(monitorName);
        if (Objects.isNull(gauge)) {
            Gauge.Builder build = Gauge.build().name(monitorName).help(monitorName);
            if (StringUtils.isNotBlank(labelName)) {
                build.labelNames(monitorName);
            }
            try {
                synchronized (gaugeNameMap) {
                    if (Objects.isNull(namesToCollectors.get(monitorName))) {
                        gauge = build.register(collectorRegistry);
                        gaugeNameMap.put(monitorName, gauge);
                    } else {
                        if (Objects.nonNull(gaugeNameMap.get(monitorName))) {
                            gauge = gaugeNameMap.get(monitorName);
                        } else {
                            gauge = build.create();
                            gaugeNameMap.put(monitorName, gauge);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Monitor_register_error", e);
            }
            return gauge;
        }

        return gauge;
    }

    private static String replaceName(String monitorName) {
        return monitorName.replaceAll("\\.", "_").replaceAll(":", "").replaceAll("/", "");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Monitor.collectorRegistry = SpringContext.getBean(CollectorRegistry.class);
        Field field = collectorRegistry.getClass().getDeclaredField("namesToCollectors");
        field.setAccessible(true);
        namesToCollectors = (Map<String, Collector>) field.get(collectorRegistry);
    }

}
