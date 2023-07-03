package com.huiyu.service.core.service.business.impl;


import com.huiyu.service.core.Hconfig.config.HotFileConfig;
import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.constant.InviteStatusEnum;
import com.huiyu.service.core.entity.Invite;
import com.huiyu.service.core.service.InviteService;
import com.huiyu.service.core.service.business.IntegralRecordBusiness;
import com.huiyu.service.core.service.business.InviteBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class InviteBusinessImpl implements InviteBusiness {

    @Resource
    private InviteService inviteService;

    @Resource
    private IntegralRecordBusiness integralRecordBusiness;

    @Resource
    private HotFileConfig hotFileConfig;

    @Override
    public boolean inviteUser(Long invitersId, String inviteesOpenid) {
        // 1. 查询是否邀请过
        Invite invite = Invite.builder()
                .invitersId(invitersId)
                .inviteesOpenid(inviteesOpenid)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        List<Invite> invites = inviteService.queryAll(invite);
        if (invites != null && !invites.isEmpty()) {
            return false;
        }
        // 2. 添加邀请记录
        invite.setIsDelete(0);
        invite.setStatus(InviteStatusEnum.UN_SUCCESS);
        return inviteService.insert(invite);
    }

    @Override
    public void invite(String inviteesOpenid) {
        // 1. 查询用户的所有邀请人
        Invite invite = Invite.builder()
                .inviteesOpenid(inviteesOpenid)
                .status(InviteStatusEnum.UN_SUCCESS)
                .build();
        List<Invite> invites = inviteService.queryAll(invite);
        if (invites == null || invites.isEmpty()) {
            return;
        }
        // 2. 更新所有邀请人的积分
        LocalDateTime now = LocalDateTime.now();
        invites.forEach(v -> {
            integralRecordBusiness.updateIntegral(v.getInvitersId(), hotFileConfig.getInviteIntegral(), IntegralSourceRecordEnum.INVITE_USER, IntegralOperationRecordEnum.ADD);
            v.setUpdateTime(now);
            v.setStatus(InviteStatusEnum.SUCCESS);
        });
        // 3. 更新所有邀请记录状态
        invites.forEach(v -> inviteService.update(v));
    }
}
