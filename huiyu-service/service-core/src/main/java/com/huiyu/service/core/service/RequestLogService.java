package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.RequestLog;
import com.huiyu.service.core.model.query.RequestLogQuery;
import org.springframework.scheduling.annotation.Async;

public interface RequestLogService extends IService<RequestLog> {

    /**
     * 后台管理分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<RequestLog> adminPageQuery(IPage<RequestLog> page, RequestLogQuery query);

    @Async
    void insert(RequestLog requestLog);
}
