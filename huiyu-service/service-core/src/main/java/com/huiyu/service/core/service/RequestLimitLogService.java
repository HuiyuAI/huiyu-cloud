package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.RequestLimitLog;
import com.huiyu.service.core.model.query.RequestLimitLogQuery;
import org.springframework.scheduling.annotation.Async;

/**
 * (RequestLimitLog)服务接口
 *
 * @author Naccl
 * @date 2023-07-05
 */
public interface RequestLimitLogService extends IService<RequestLimitLog> {

    /**
     * 后台管理分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<RequestLimitLog> adminPageQuery(IPage<RequestLimitLog> page, RequestLimitLogQuery query);

    @Async
    void insert(RequestLimitLog requestLimitLog);
}
