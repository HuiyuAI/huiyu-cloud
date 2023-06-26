package com.huiyu.service.core.constant;

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

    BACK(3, "BACK", "回退");

    private Integer code;

    private String dictKey;

    private String desc;
}
