package com.huiyu.service.core.service.impl;

import com.huiyu.service.core.entity.Invite;
import com.huiyu.service.core.mapper.InviteMapper;
import com.huiyu.service.core.service.InviteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InviteServiceImpl implements InviteService {

    @Resource
    private InviteMapper inviteMapper;

    @Override
    public boolean insert(Invite invite) {
        return inviteMapper.insert(invite) > 0;
    }

    @Override
    public boolean update(Invite invite) {
        return inviteMapper.update(invite) > 0;
    }

    @Override
    public List<Invite> queryAll(Invite invite) {
        return inviteMapper.queryAll(invite);
    }

}
