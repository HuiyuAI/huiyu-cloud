package com.huiyu.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应实体
 *
 * @author Naccl
 * @date 2021-12-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public static <T> R<T> create(IErrorCode errorCode) {
        return new R(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> R<T> create(IErrorCode errorCode, T data) {
        return new R(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static <T> R<T> ok() {
        return R.create(ResultCode.SUCCESS);
    }

    public static <T> R<T> ok(T data) {
        return R.create(ResultCode.SUCCESS, data);
    }

    public static <T> R<T> ok(String msg, T data) {
        return new R(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> R<T> error() {
        return R.create(ResultCode.FAILED);
    }

    public static <T> R<T> error(String msg) {
        return new R(ResultCode.FAILED.getCode(), msg, null);
    }

    public static <T> R<T> status(boolean flag) {
        return flag ? R.ok() : R.error();
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.getCode());
    }
}
