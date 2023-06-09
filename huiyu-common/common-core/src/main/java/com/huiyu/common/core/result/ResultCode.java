package com.huiyu.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 响应码枚举
 *
 * @author Naccl
 * @date 2021-12-08
 */
@Getter
@ToString
@AllArgsConstructor
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "异常错误"),
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    ;

    private Integer code;
    private String message;
}
