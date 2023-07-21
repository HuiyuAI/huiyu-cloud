package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.RequestLimitLog;
import com.huiyu.service.core.mapper.RequestLimitLogMapper;
import com.huiyu.service.core.service.RequestLimitLogService;
import org.springframework.stereotype.Service;

/**
 * (RequestLimitLog)服务实现类
 *
 * @author Naccl
 * @date 2023-07-05
 */
@Service
public class RequestLimitLogServiceImpl extends ServiceImpl<RequestLimitLogMapper, RequestLimitLog> implements RequestLimitLogService {

    @Override
    public void insert(RequestLimitLog requestLimitLog) {
        super.save(requestLimitLog);
    }
}
