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
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求体
     */
    private String body;
    /**
     * 任务状态
     */
    private Integer status;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    private Integer isDelete;

    private String execSource;
}
