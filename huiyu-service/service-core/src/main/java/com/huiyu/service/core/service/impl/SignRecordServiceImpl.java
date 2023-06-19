package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.SignRecord;
import com.huiyu.service.core.mapper.SignRecordMapper;
import com.huiyu.service.core.service.SignRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SignRecordServiceImpl implements SignRecordService {

    @Resource
    private SignRecordMapper signRecordMapper;

    @Override
    public SignRecord getTodayByUserId(Long userId) {
        return signRecordMapper.getTodayByUserId(userId);
    }

    @Override
    public boolean insert(SignRecord signRecord) {
        return signRecordMapper.insert(signRecord) > 0;
    }

    @Override
    public boolean updateToday(SignRecord signRecord) {
        return signRecordMapper.updateToday(signRecord) > 0;
    }

    @Override
    public boolean deleteTodayByUserId(Long userId) {
        return signRecordMapper.deleteTodayByUserId(userId) > 0;
    }
}
