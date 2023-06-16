package com.huiyu.service.core.config;

import org.springframework.stereotype.Component;

@Component
public class ThreadLocalConfig {
    private final static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public Object get() {
        return threadLocal.get();
    }

    //设置当前线程的存的变量
    public void set(Object object) {
        threadLocal.set(object);
    }

    //移除当前线程的存的变量
    public void remove() {
        threadLocal.remove();
    }
}
