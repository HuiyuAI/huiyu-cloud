package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.PointRecord;
import com.huiyu.service.core.mapper.PointRecordMapper;
import com.huiyu.service.core.service.PointRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointRecordServiceImpl extends ServiceImpl<PointRecordMapper, PointRecord> implements PointRecordService {

    @Override
    public boolean insertRecord(PointRecord pointRecord) {
        return super.save(pointRecord);
    }

    @Override
    public List<PointRecord> getRecordByUserId(Long userId) {
        return super.baseMapper.getByUserId(userId);
    }

}
