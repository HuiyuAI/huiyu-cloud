package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.model.dto.PointRecordPageDto;
import com.huiyu.service.core.model.query.PointRecordQuery;

public interface PointRecordService extends IService<PointRecord> {

    /**
     * 后台管理分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<PointRecord> adminPageQuery(IPage<PointRecord> page, PointRecordQuery query);

    /**
     * 用户积分记录分页查询
     *
     * @param page 分页对象
     * @param dto  筛选条件
     * @return 查询结果
     */
    IPage<PointRecord> pagePointRecord(IPage<PointRecord> page, PointRecordPageDto dto);

    /**
     * 根据请求uuid查询积分流水
     *
     * @param requestUuid 请求uuid
     * @return 积分流水
     */
    PointRecord getByRequestUuid(String requestUuid);

    /**
     * 新增积分记录
     *
     * @param pointRecord 积分记录
     * @return 新增结果
     */
    boolean insertRecord(PointRecord pointRecord);

    /**
     * 更新积分记录
     *
     * @param pointRecord 积分记录
     * @return true/false
     */
    boolean updatePointRecord(PointRecord pointRecord);

}
