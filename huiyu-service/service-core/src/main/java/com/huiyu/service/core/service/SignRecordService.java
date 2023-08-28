package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.SignRecord;

import java.time.LocalDateTime;

public interface SignRecordService extends IService<SignRecord> {

    /**
     * 判断用户是否已签到
     *
     * @param userId 用户id
     * @param start  开始时间
     * @param end    结束时间
     * @return true/false
     */
    boolean isSignIn(Long userId, LocalDateTime start, LocalDateTime end);

    /**
     * 保存签到记录
     *
     * @param signRecord 签到记录
     * @return true/false
     */
    boolean save(SignRecord signRecord);

}
