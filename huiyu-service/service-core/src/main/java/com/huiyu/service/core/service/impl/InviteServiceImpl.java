package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.Invite;
import com.huiyu.service.core.mapper.InviteMapper;
import com.huiyu.service.core.service.InviteService;
import org.springframework.stereotype.Service;

@Service
public class InviteServiceImpl extends ServiceImpl<InviteMapper, Invite> implements InviteService {

    @Override
    public boolean insert(Invite invite) {
        return super.save(invite);
    }

    @Override
    public Invite getByInviteeId(Long userId) {
        return super.lambdaQuery()
                .eq(Invite::getInviteeId, userId)
                .one();
    }

}
