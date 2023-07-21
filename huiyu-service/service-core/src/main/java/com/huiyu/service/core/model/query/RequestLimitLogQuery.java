package com.huiyu.service.core.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-07-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestLimitLogQuery {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * ip
     */
    private String ip;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 请求url
     */
    private String uri;
    /**
     * 请求时间开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 请求时间结束
     */
    private LocalDateTime createTimeEnd;
}
