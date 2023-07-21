package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.PointRecord;

import java.util.List;

public interface PointRecordService extends IService<PointRecord> {

    boolean insertRecord(PointRecord pointRecord);

    List<PointRecord> getRecordByUserId(Long userId);

}
