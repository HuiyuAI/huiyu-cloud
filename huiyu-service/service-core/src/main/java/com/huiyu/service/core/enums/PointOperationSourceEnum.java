package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum PointOperationSourceEnum implements BaseEnum<String> {
    REGISTER("register", "注册"),
    GENERATE_PIC("generatePic", "生成图片"),
    SIGN_IN("signIn", "每日登录"),
    FAIL_RETURN("failReturn", "任务失败返还"),
    INVITE_USER("inviteUser", "邀请用户"),
    ADMIN_UPDATE("adminUpdate", "管理员修改"),
    ;

    private String dictKey;

    private String desc;
}
