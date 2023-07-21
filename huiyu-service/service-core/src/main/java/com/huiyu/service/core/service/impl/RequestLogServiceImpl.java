package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.RequestLog;
import com.huiyu.service.core.mapper.RequestLogMapper;
import com.huiyu.service.core.model.query.RequestLogQuery;
import com.huiyu.service.core.service.RequestLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RequestLogServiceImpl extends ServiceImpl<RequestLogMapper, RequestLog> implements RequestLogService {

    @Override
    public IPage<RequestLog> adminPageQuery(IPage<RequestLog> page, RequestLogQuery query) {
        return super.lambdaQuery()
                .eq(query.getId() != null, RequestLog::getId, query.getId())
                .eq(query.getUserId() != null, RequestLog::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getIp()), RequestLog::getIp, query.getIp())
                .eq(StringUtils.isNotEmpty(query.getMethod()), RequestLog::getMethod, query.getMethod())
                .eq(StringUtils.isNotEmpty(query.getUri()), RequestLog::getUri, query.getUri())
                .ge(query.getElapsedTimeGE() != null, RequestLog::getElapsedTime, query.getElapsedTimeGE())
                .ge(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, RequestLog::getCreateTime, query.getCreateTimeStart())
                .le(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, RequestLog::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(RequestLog::getId)
                .page(page);
    }

    @Override
    public void insert(RequestLog requestLog) {
        super.save(requestLog);
    }
}
