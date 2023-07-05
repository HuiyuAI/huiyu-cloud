package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.RequestLog;
import org.springframework.scheduling.annotation.Async;

public interface RequestLogService {

    @Async
    void insert(RequestLog requestLog);
}
