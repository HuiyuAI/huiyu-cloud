package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (RequestLimit)实体类
 *
 * @author Naccl
 * @date 2023-07-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("request_limit")
public class RequestLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * ip
     */
    @TableField("ip")
    private String ip;
    /**
     * 请求方式
     */
    @TableField("method")
    private String method;
    /**
     * 请求接口
     */
    @TableField("uri")
    private String uri;
    /**
     * 请求参数
     */
    @TableField("param")
    private String param;
    /**
     * 请求时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
