package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 每日任务枚举
 *
 * @author Naccl
 * @date 2023-08-29
 */
@Getter
@ToString
@AllArgsConstructor
public enum DailyTaskEnum implements BaseEnum<String> {
    SIGN_IN("signIn", "每日签到", 100, 1),
    INVITE_USER("inviteUser", "邀请好友", 100, -1),
    GENERATE_PIC("generatePic", "完成创作", 10, 5),
    SHARE_PIC("sharePic", "投稿作品", 10, 3),
    ;

    private String dictKey;

    private String desc;
    /**
     * 默认任务奖励积分（热配）
     */
    private Integer point;
    /**
     * 需完成次数
     */
    private Integer count;
}
