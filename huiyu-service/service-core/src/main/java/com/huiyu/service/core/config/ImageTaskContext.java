package com.huiyu.service.core.config;

import com.huiyu.service.core.entity.Task;

public class ImageTaskContext {
    public final static InheritableThreadLocal<Task> TASK_INFO_CONTEXT = new InheritableThreadLocal<>();

}
