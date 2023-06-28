package com.huiyu.service.core.service.business;

public interface SignRecordBusiness {

    /**
     * 签到
     *
     * @param userId    用户id
     * @return
     */
    boolean signIn(Long userId);
}
