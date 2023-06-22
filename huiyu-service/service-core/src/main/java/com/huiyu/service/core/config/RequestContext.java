package com.huiyu.service.core.config;

public class RequestContext {
    public final static InheritableThreadLocal<String> REQUEST_UUID_CONTEXT = new InheritableThreadLocal<>();

}
