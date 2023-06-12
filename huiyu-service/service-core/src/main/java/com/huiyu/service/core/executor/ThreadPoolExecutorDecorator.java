package com.huiyu.service.core.executor;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wAnG
 * @Date 2023-06-13  00:24
 */
@Data
@Builder
public class ThreadPoolExecutorDecorator {

    private ThreadPoolExecutor threadPoolExecutor;

    private String sourceName;

}
