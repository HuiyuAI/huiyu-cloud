package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.Invite;

public interface InviteService extends IService<Invite> {
    boolean insert(Invite invite);

    /**
     * 根据用户id查询被邀请记录
     *
     * @param userId 用户id
     * @return 被邀请记录
     */
    Invite getByInviteeId(Long userId);
}
