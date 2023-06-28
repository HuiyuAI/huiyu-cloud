package com.huiyu.service.core.service.business;

public interface InviteBusiness {
    /**
     * 邀请用户
     *
     * @param invitersId        邀请人id
     * @param inviteesOpenid    被邀请人openid
     * @return
     */
    boolean inviteUser(Long invitersId, String inviteesOpenid);

    /**
     * 用户注册 邀请人积分奖励
     *
     * @param inviteesOpenid    用户openid
     */
    void invite(String inviteesOpenid);
}
