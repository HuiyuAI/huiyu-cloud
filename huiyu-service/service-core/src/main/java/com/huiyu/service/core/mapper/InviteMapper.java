package com.huiyu.service.core.mapper;


import com.huiyu.service.core.entity.Invite;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InviteMapper {

    int insert(Invite invite);

    int update(Invite invite);

    List<Invite> queryAll(Invite invite);

//    int updateBatchById(List<Invite> invites);
}
