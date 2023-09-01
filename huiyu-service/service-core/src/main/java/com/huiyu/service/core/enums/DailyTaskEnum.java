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
    SIGN_IN("signIn", "每日签到", 100, 1, 1),
    GENERATE_PIC("generatePic", "完成创作", 10, 5, 1),
    SHARE_PIC("sharePic", "投稿作品", 10, 3, 1),
    SHARE_PASS("sharePass", "投稿通过", 5, -1, 5),
    INVITE_USER("inviteUser", "邀请好友", 100, -1, -1),
    ;

    private String dictKey;

    private String desc;
    /**
     * 默认任务奖励积分（热配）
     */
    private Integer point;
    /**
     * 每轮需完成次数，-1为触发即认为完成且不显示次数，1为每轮只需完成1次且显示次数(0/1)
     */
    private Integer countPerRound;
    /**
     * 每天可完成轮数，-1为每天可无限次完成
     */
    private Integer roundPerDay;
}
