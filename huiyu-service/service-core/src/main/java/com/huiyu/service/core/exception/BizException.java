package com.huiyu.service.core.exception;

/**
 * @author Naccl
 * @date 2023-08-11
 */
public class BizException extends RuntimeException {
    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
