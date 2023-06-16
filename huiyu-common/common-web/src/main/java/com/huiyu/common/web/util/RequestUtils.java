package com.huiyu.common.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Naccl
 * @date 2023-06-17
 */
public class RequestUtils {

    public static String getRequestURI() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRequestURI();
    }
}
