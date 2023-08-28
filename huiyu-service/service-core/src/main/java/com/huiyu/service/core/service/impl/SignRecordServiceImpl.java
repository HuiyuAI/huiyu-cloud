package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.SignRecord;
import com.huiyu.service.core.mapper.SignRecordMapper;
import com.huiyu.service.core.service.SignRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SignRecordServiceImpl extends ServiceImpl<SignRecordMapper, SignRecord> implements SignRecordService {

    @Override
    public boolean isSignIn(Long userId, LocalDateTime start, LocalDateTime end) {
        return super.baseMapper.countByUserIdAndSignTime(userId, start, end) > 0;
    }

    @Override
    public boolean save(SignRecord signRecord) {
        return super.save(signRecord);
    }

}
