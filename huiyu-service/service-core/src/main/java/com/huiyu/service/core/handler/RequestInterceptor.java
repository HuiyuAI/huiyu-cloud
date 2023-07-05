package com.huiyu.service.core.handler;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.utils.IpAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: wAnG
 * @Date: 2021/11/26 16:43
 * @Description: 请求拦截器，打印请求和响应日志
 */

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger("paramLog");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String IP = IpAddressUtils.getIpAddress(request);
        String remoteName = request.getServerName();
        long startTime = System.currentTimeMillis();
        request.getSession().setAttribute("startTime", startTime);
        log.info("\n" +
                        "==================== RequestStart ====================\n" +
                        "IP : [{}] | Method : [{}] | URI : [{}] | remoteName : [{}]\n" +
                        "====================  RequestEnd  ====================",
                IP, method, requestURI, remoteName);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        int status = response.getStatus();
        Object body = request.getSession().getAttribute("body");
        Long startTime = (Long) request.getSession().getAttribute("startTime");
        Long endTime = System.currentTimeMillis();
        Long totalTime = endTime - startTime;
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String IP = IpAddressUtils.getIpAddress(request);
        String remoteName = request.getServerName();
        log.info("\n" +
                        "==================== ResponseStart ====================\n" +
                        "totalTime : {}ms ==> IP : [{}] | Method : [{}] | URI : [{}] | remoteName : [{}] |\n" +
                        "status : [{}] | body : [{}]\n" +
                        "====================  ResponseEnd  ====================",
                totalTime, IP, method, requestURI, remoteName, status, JacksonUtils.toJsonStr(body));
    }

}
