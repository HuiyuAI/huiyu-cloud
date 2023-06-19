package com.huiyu.service.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author: 陈瑾
 * @date: 2023/6/19 16:37
 * @Description: 签到状态
 **/
@Getter
@ToString
@AllArgsConstructor
public enum SignRecordStatusEnum implements BaseEnum<Integer> {
    UN_SIGN_IN(0, "未签到"),

    SIGN_IN(1, "已签到"),
    ;

    private Integer dictKey;

    private String desc;
}
