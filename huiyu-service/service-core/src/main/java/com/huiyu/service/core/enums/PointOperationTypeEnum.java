package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum PointOperationTypeEnum implements BaseEnum<Integer> {

    REDUCE(0, "减少"),
    ADD(1, "增加"),
    WAIT_CALC(100, "待计算"),
    ;

    private Integer dictKey;

    private String desc;
}
