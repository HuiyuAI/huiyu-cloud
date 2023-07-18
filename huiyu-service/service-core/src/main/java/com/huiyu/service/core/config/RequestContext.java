package com.huiyu.service.core.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.cmd.Cmd;

public class RequestContext {
    public final static TransmittableThreadLocal<String> REQUEST_UUID_CONTEXT = new TransmittableThreadLocal<>();

    public final static TransmittableThreadLocal<Cmd> CMD_CONTEXT = new TransmittableThreadLocal<>();

    public final static TransmittableThreadLocal<Model> MODEL_CONTEXT = new TransmittableThreadLocal<>();

    public final static TransmittableThreadLocal<Pic> PARENT_PIC_CONTEXT = new TransmittableThreadLocal<>();

}
