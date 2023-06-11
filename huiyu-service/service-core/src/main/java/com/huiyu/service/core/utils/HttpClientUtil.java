package com.huiyu.service.core.utils;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author wAnG
 * @date 2023-06-11  21:45
 */
@Component
public class HttpClientUtil implements DisposableBean {

    private final PoolingHttpClientConnectionManager manager = test();

    public static PoolingHttpClientConnectionManager test(){
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
            return poolingHttpClientConnectionManager;
    }

    @Override
    public void destroy() throws Exception {
            manager.close();
    }
}
