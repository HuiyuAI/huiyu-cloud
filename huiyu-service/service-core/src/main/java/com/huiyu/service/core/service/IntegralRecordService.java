package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.IntegralRecord;

import java.util.List;

public interface IntegralRecordService {

    boolean insertRecord(IntegralRecord integralRecord);

    List<IntegralRecord> getRecordByUserId(String userId);

    boolean updateRecord(IntegralRecord integralRecord);

    boolean delete(IntegralRecord integralRecord);
}
