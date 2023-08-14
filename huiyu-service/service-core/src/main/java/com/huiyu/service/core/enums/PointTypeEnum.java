package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 积分类型枚举
 *
 * @author Naccl
 * @date 2023-08-14
 */
@Getter
@ToString
@AllArgsConstructor
public enum PointTypeEnum implements BaseEnum<String> {

    DAILY_POINT("dailyPoint", "每日积分"),
    POINT("point", "永久积分"),
    MIX_POINT("mixPoint", "混合积分"),
    ;

    private String dictKey;

    private String desc;
}
