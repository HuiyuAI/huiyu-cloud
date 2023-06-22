package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.IntegralRecord;
import com.huiyu.service.core.mapper.IntegralRecordMapper;
import com.huiyu.service.core.service.IntegralRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IntegralRecordServiceImpl implements IntegralRecordService {

    @Resource
    private IntegralRecordMapper integralRecordMapper;

    @Override
    public boolean insertRecord(IntegralRecord integralRecord) {
        int result = integralRecordMapper.insertRecord(integralRecord);
        return result > 0;
    }

    @Override
    public List<IntegralRecord> getRecordByUserId(Long userId) {
        return integralRecordMapper.getByUserId(userId);
    }

}
