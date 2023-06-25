package com.huiyu.service.core.config.executor;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.Executor;

/**
 * @author wAnG
 * @Date 2023-06-13  00:24
 */
@Data
@Builder
public class ThreadPoolExecutorDecorator {

    private Executor threadPoolExecutor;

    private String sourceName;

    private String ip;

}
