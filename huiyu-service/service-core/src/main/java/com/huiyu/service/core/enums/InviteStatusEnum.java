package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum InviteStatusEnum implements BaseEnum<Integer> {
    UN_SUCCESS(0,"未成功"),

    SUCCESS(1,"已成功"),
            ;

    private Integer dictKey;

    private String desc;
}
