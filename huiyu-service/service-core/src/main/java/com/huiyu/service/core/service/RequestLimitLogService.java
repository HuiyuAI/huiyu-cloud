package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.RequestLimitLog;
import org.springframework.scheduling.annotation.Async;

/**
 * (RequestLimitLog)服务接口
 *
 * @author Naccl
 * @date 2023-07-05
 */
public interface RequestLimitLogService extends IService<RequestLimitLog> {

    @Async
    void insert(RequestLimitLog requestLimitLog);
}
