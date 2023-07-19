package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum StateEnum implements BaseEnum<Integer> {

    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer dictKey;

    private String desc;

}
