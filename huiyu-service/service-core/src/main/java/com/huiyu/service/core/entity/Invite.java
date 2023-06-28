package com.huiyu.service.core.entity;

import com.huiyu.service.core.constant.IntegralOperationRecordEnum;
import com.huiyu.service.core.constant.IntegralSourceRecordEnum;
import com.huiyu.service.core.constant.InviteStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invite implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 邀请人id
     */
    private Long invitersId;
    /**
     * 被邀请人openid
     */
    private String inviteesOpenid;
    /**
     * 未成功0，已成功1
     */
    private InviteStatusEnum status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    private Integer isDelete;
}
