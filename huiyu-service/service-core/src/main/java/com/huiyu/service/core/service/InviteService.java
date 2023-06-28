package com.huiyu.service.core.service;

import com.huiyu.service.core.entity.Invite;

import java.util.List;

public interface InviteService {
    boolean insert(Invite invite);

    boolean update(Invite invite);

    List<Invite> queryAll(Invite invite);
}
