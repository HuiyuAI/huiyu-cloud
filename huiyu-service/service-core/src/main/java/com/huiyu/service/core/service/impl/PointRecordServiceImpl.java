package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.mapper.PointRecordMapper;
import com.huiyu.service.core.service.PointRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PointRecordServiceImpl implements PointRecordService {

    @Resource
    private PointRecordMapper pointRecordMapper;

    @Override
    public boolean insertRecord(PointRecord pointRecord) {
        int result = pointRecordMapper.insertRecord(pointRecord);
        return result > 0;
    }

    @Override
    public List<PointRecord> getRecordByUserId(Long userId) {
        return pointRecordMapper.getByUserId(userId);
    }

}
