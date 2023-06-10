package com.huiyu.service.core.filter;

import com.huiyu.service.core.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: wAnG
 * @Date: 2023-06-11  01:31
 */
@Slf4j
@Component
@WebFilter(filterName = "traceIdFilter",urlPatterns = "/*")
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID = "TraceId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.put(TRACE_ID, IdUtils.getTraceId());
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
