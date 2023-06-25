package com.huiyu.service.core.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.huiyu.service.core.entity.Task;

public class TaskContext {
    public static final TransmittableThreadLocal<Task> TASK_INFO_CONTEXT = new TransmittableThreadLocal<>();

    public static final TransmittableThreadLocal<Task> TASK_SUBMIT_CONTEXT = new TransmittableThreadLocal<>();

    public static final TransmittableThreadLocal<String> TASK_THREAD_POOL_NAME = new TransmittableThreadLocal<>();

    public final static InheritableThreadLocal<String> INVOKER_URL_CONTEXT = new InheritableThreadLocal<>();

}
