package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.RequestLog;
import com.huiyu.service.core.mapper.RequestLogMapper;
import com.huiyu.service.core.service.RequestLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RequestLogServiceImpl implements RequestLogService {

    @Resource
    private RequestLogMapper requestLogMapper;

    @Override
    public void insert(RequestLog requestLog) {
        requestLogMapper.insert(requestLog);
    }
}
