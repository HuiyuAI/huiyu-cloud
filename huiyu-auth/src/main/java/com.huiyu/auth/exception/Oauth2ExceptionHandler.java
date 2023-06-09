package com.huiyu.auth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.huiyu.common.core.result.R;

/**
 * 全局处理Oauth2抛出的异常
 *
 * @author Naccl
 * @date 2022-03-01
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {
    @ExceptionHandler(value = OAuth2Exception.class)
    public R handleOauth2(OAuth2Exception e) {
        return R.error(e.getMessage());
    }
}
