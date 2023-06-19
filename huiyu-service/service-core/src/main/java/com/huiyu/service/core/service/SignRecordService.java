package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.SignRecord;

public interface SignRecordService {

    SignRecord getTodayByUserId(Long userId);

    boolean insert(SignRecord signRecord);

    boolean updateToday(SignRecord signRecord);

    boolean deleteTodayByUserId(Long userId);
}
