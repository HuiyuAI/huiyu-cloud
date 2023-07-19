package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.PointRecord;

import java.util.List;

public interface PointRecordService {

    boolean insertRecord(PointRecord pointRecord);

    List<PointRecord> getRecordByUserId(Long userId);

}
