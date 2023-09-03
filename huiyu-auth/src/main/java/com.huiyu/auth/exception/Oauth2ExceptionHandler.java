package com.huiyu.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.huiyu.common.core.result.R;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局处理Oauth2抛出的异常
 *
 * @author Naccl
 * @date 2022-03-01
 */
@Slf4j
@RestControllerAdvice
public class Oauth2ExceptionHandler {
    @ExceptionHandler(value = OAuth2Exception.class)
    public R handleOauth2(OAuth2Exception e) {
        log.error("OAuth2Exception Exception:", e);
        return R.error();
    }

    @ExceptionHandler(Exception.class)
    public <T> R<T> exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("Request URL : {}, Exception :", request.getRequestURL(), e);
        return R.error();
    }
}
