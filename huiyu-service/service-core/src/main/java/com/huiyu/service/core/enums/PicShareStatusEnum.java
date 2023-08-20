package com.huiyu.service.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 图片投稿状态枚举
 *
 * @author Naccl
 * @date 2023-08-19
 */
@Getter
@ToString
@AllArgsConstructor
public enum PicShareStatusEnum implements BaseEnum<String> {
    NONE("none", "未分享"),
    AUDITING("auditing", "审核中"),
    PUBLIC("public", "已公开"),
    REJECT("reject", "审核未通过"),
    CANCEL("cancel", "已取消投稿"),
    ;

    private String dictKey;

    private String desc;
}
