package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.RequestLimit;
import org.springframework.scheduling.annotation.Async;

/**
 * (RequestLimit)服务接口
 *
 * @author Naccl
 * @date 2023-07-05
 */
public interface RequestLimitService extends IService<RequestLimit> {

    @Async
    void insert(RequestLimit requestLimit);
}
