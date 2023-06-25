package com.huiyu.service.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求url
     */
    private String url;
    /**
     * 请求时间
     */
    private LocalDateTime createTime;

}