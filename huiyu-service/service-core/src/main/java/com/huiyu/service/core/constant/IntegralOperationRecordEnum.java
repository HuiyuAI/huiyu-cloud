package com.huiyu.service.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum IntegralOperationRecordEnum implements BaseEnum<Integer> {

    REDUCE(0, "减少"),
    ADD(1, "增加");

    private Integer dictKey;

    private String desc;
}
