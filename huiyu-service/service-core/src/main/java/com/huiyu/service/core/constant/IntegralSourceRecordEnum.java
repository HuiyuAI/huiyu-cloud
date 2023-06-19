package com.huiyu.service.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum IntegralSourceRecordEnum implements BaseEnum<String> {
    REGISTER(0, "register", "注册"),
    GENERATE_PIC(1, "generate_pic", "生成图片");

    private Integer code;

    private String dictKey;

    private String desc;
}
