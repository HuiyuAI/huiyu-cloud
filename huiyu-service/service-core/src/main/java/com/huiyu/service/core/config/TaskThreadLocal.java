package com.huiyu.service.core.config;

import com.huiyu.service.core.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskThreadLocal {
    private final static ThreadLocal<Task> threadLocal = new ThreadLocal<>();

    public static Task get() {
        return threadLocal.get();
    }

    public static void set(Task object) {
        threadLocal.set(object);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
