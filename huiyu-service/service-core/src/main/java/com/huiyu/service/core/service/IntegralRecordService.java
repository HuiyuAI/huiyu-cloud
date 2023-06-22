package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.IntegralRecord;

import java.util.List;

public interface IntegralRecordService {

    boolean insertRecord(IntegralRecord integralRecord);

    List<IntegralRecord> getRecordByUserId(Long userId);

}
