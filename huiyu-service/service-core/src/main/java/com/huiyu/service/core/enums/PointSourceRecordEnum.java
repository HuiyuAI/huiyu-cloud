package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum PointSourceRecordEnum implements BaseEnum<String> {
    REGISTER("register", "注册"),
    GENERATE_PIC("generate_pic", "生成图片"),
    SIGN_IN("sign_in", "签到"),
    BACK("back", "回退"),
    INVITE_USER("invite_user", "邀请用户"),
    ;

    private String dictKey;

    private String desc;
}
