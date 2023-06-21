package com.huiyu.common.web.handler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.huiyu.common.core.result.R;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletionException;

/**
 * 对Controller全局异常处理
 *
 * @author Naccl
 * @date 2021-12-10
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final String EXCEPTION_MSG_FEIGN = "feign.FeignException";

    @ExceptionHandler(CompletionException.class)
    public <T> R<T> processException(HttpServletRequest request, CompletionException e) {
        if (e.getMessage().startsWith(EXCEPTION_MSG_FEIGN)) {
            log.error("微服务调用异常: {}", e.getMessage());
            return R.error("微服务调用异常");
        }
        return exceptionHandler(request, e);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public <T> R<T> processException(FeignException.BadRequest e) {
        log.error("微服务feign调用异常: {}", e.getMessage());
        return R.error(e.getMessage());
    }

    /**
     * 捕获断言异常
     *
     * @param e   断言异常
     * @param <T>
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public <T> R<T> processException(IllegalArgumentException e) {
        log.warn("断言异常: {}", e.getMessage());
        return R.error(e.getMessage());
    }

    /**
     * 捕获参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> R<T> processException(MethodArgumentNotValidException e) {
        log.warn("参数校验异常: {}", e.getMessage());
        return R.error(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 捕获其它异常
     *
     * @param request 请求
     * @param e       异常信息
     * @return
     */
    @ExceptionHandler(Exception.class)
    public <T> R<T> exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("Request URL : {}, Exception :", request.getRequestURL(), e);
        return R.error();
    }
}
