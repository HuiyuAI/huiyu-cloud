package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.RequestLimitLog;
import com.huiyu.service.core.mapper.RequestLimitLogMapper;
import com.huiyu.service.core.model.query.RequestLimitLogQuery;
import com.huiyu.service.core.service.RequestLimitLogService;
import org.apache.commons.lang3.StringUtils;
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
    public IPage<RequestLimitLog> adminPageQuery(IPage<RequestLimitLog> page, RequestLimitLogQuery query) {
        return super.lambdaQuery()
                .eq(query.getId() != null, RequestLimitLog::getId, query.getId())
                .eq(query.getUserId() != null, RequestLimitLog::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getIp()), RequestLimitLog::getIp, query.getIp())
                .eq(StringUtils.isNotEmpty(query.getMethod()), RequestLimitLog::getMethod, query.getMethod())
                .eq(StringUtils.isNotEmpty(query.getUri()), RequestLimitLog::getUri, query.getUri())
                .ge(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, RequestLimitLog::getCreateTime, query.getCreateTimeStart())
                .le(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, RequestLimitLog::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(RequestLimitLog::getId)
                .page(page);
    }

    @Override
    public void insert(RequestLimitLog requestLimitLog) {
        super.save(requestLimitLog);
    }
}
