package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum IntegralSourceRecordEnum implements BaseEnum<String> {
    REGISTER(0, "register", "注册"),
    GENERATE_PIC(1, "generate_pic", "生成图片"),
    SIGN_IN(2, "sign_in", "签到"),
    BACK(3, "back", "回退"),
    INVITE_USER(4, "invite_user", "邀请用户");

    private Integer code;

    private String dictKey;

    private String desc;
}
