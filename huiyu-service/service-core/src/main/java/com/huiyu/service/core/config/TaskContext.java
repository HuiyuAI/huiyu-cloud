package com.huiyu.service.core.config;

import com.huiyu.service.core.entity.Task;

public class TaskContext {
    public static final InheritableThreadLocal<Task> TASK_INFO_CONTEXT = new InheritableThreadLocal<>();

    public static final InheritableThreadLocal<Task> TASK_SUBMIT_CONTEXT = new InheritableThreadLocal<>();

    public static final InheritableThreadLocal<String> TASK_THREAD_POOL_NAME = new InheritableThreadLocal<>();

    public final static InheritableThreadLocal<String> INVOKER_URL_CONTEXT = new InheritableThreadLocal<>();

}
