package com.huiyu.service.core.config.executor;

import com.huiyu.service.core.config.Monitor;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author wAnG
 * @Date 2023-06-18  02:00
 */
public class MonitorLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {

    private static final String QUEUE_MONITOR_SUFFIX = "_queue_count";

    private String monitorName;

    public MonitorLinkedBlockingQueue(String monitorName) {
        this.monitorName = monitorName;
    }

    public MonitorLinkedBlockingQueue(int capacity, String monitorName) {
        super(capacity);
        this.monitorName = monitorName;
    }

    public MonitorLinkedBlockingQueue(Collection<? extends E> c, String monitorName) {
        super(c);
        this.monitorName = monitorName;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        boolean offer = super.offer(e, timeout, unit);
        if (offer) {
            Monitor.recordInc(monitorName + QUEUE_MONITOR_SUFFIX);
        }
        return offer;
    }

    @Override
    public boolean offer(E e) {
        boolean offer = super.offer(e);
        if (offer) {
            Monitor.recordInc(monitorName + QUEUE_MONITOR_SUFFIX);
        }
        return offer;
    }


    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E poll = super.poll(timeout, unit);
        if (Objects.nonNull(poll)) {
            Monitor.recordDec(monitorName + QUEUE_MONITOR_SUFFIX);
        }
        return poll;
    }

    @Override
    public E take() throws InterruptedException {
        E take = super.take();
        Monitor.recordDec(monitorName + QUEUE_MONITOR_SUFFIX);
        return take;
    }
}
