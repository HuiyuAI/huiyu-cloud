package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.SignRecord;

import java.time.LocalDate;

public interface SignRecordService extends IService<SignRecord> {

    /**
     * 判断用户是否已签到
     *
     * @param userId   用户id
     * @param signDate 签到日期
     * @return true/false
     */
    boolean isSignIn(Long userId, LocalDate signDate);

    /**
     * 保存签到记录
     *
     * @param signRecord 签到记录
     * @return true/false
     */
    boolean save(SignRecord signRecord);

}
