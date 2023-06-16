package com.huiyu.service.core.executor;

import lombok.Builder;
import lombok.Data;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author wAnG
 * @Date 2023-06-13  00:24
 */
@Data
@Builder
public class ThreadPoolExecutorDecorator {

    private ThreadPoolTaskExecutor threadPoolExecutor;

    private String sourceName;

}
