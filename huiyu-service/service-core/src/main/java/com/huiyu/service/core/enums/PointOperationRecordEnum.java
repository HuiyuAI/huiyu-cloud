package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum PointOperationRecordEnum implements BaseEnum<Integer> {

    REDUCE(0, "减少"),
    ADD(1, "增加");

    private Integer dictKey;

    private String desc;
}
