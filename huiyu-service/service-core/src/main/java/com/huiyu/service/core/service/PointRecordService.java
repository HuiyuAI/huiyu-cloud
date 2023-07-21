package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.model.query.PointRecordQuery;

import java.util.List;

public interface PointRecordService extends IService<PointRecord> {

    /**
     * 后台管理分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<PointRecord> adminPageQuery(IPage<PointRecord> page, PointRecordQuery query);

    boolean insertRecord(PointRecord pointRecord);

    List<PointRecord> getRecordByUserId(Long userId);
}
