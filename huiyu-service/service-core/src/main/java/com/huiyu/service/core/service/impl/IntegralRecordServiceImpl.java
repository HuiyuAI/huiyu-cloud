package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.IntegralRecord;
import com.huiyu.service.core.mapper.integral.IntegralRecordMapper;
import com.huiyu.service.core.service.IntegralRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IntegralRecordServiceImpl implements IntegralRecordService {

    private static final Integer TRUE = 1;

    @Resource
    private IntegralRecordMapper integralRecordMapper;

    @Override
    public boolean insertRecord(IntegralRecord integralRecord) {
        int result = integralRecordMapper.insertRecord(integralRecord);
        return result > 0;
    }

    @Override
    public List<IntegralRecord> getRecordByUserId(String userId) {
        return integralRecordMapper.getByUserId(userId);
    }

    @Override
    public boolean updateRecord(IntegralRecord integralRecord) {
        int result = integralRecordMapper.update(integralRecord);
        return result > 0;
    }

    @Override
    public boolean delete(IntegralRecord integralRecord) {
        if(StringUtils.isBlank(integralRecord.getRecordNo())){
            return false;
        }
        integralRecord.setIsDelete(TRUE);
        int result = integralRecordMapper.update(integralRecord);
        return result > 0;
    }

}
