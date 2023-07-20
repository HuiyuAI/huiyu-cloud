package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum TaskStatusEnum implements BaseEnum<Integer> {
    UN_EXECUTED(0, "未执行"),

    EXECUTED(1, "已执行"),

    IN_QUEUE(2, "在队列中"),

    DISCARD(3, "废弃"),
    ;

    private Integer dictKey;

    private String desc;

}
