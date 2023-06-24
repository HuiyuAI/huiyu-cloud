package com.huiyu.service.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 图片状态枚举
 *
 * @author Naccl
 * @date 2023-06-18
 */
@Getter
@ToString
@AllArgsConstructor
public enum PicStatusEnum implements BaseEnum<Integer> {
    GENERATING(0, "生成中"),

    GENERATED(1, "已生成"),

    DISCARD(2, "废弃"),
    ;

    private Integer dictKey;

    private String desc;
}
