package com.huiyu.service.core.config;

import com.alibaba.ttl.TransmittableThreadLocal;

public class RequestContext {
    public final static TransmittableThreadLocal<String> REQUEST_UUID_CONTEXT = new TransmittableThreadLocal<>();

}
