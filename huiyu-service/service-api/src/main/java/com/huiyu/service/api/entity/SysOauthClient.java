package com.huiyu.service.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysOauthClient)实体类
 *
 * @author Naccl
 * @date 2022-03-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysOauthClient implements Serializable {
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 资源id列表
     */
    private String resourceIds;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 域
     */
    private String scope;
    /**
     * 授权方式
     */
    private String authorizedGrantTypes;
    /**
     * 回调地址
     */
    private String webServerRedirectUri;
    /**
     * 权限列表
     */
    private String authorities;
    /**
     * 认证令牌时效
     */
    private Integer accessTokenValidity;
    /**
     * 刷新令牌时效
     */
    private Integer refreshTokenValidity;
    /**
     * 扩展信息
     */
    private String additionalInformation;
    /**
     * 是否自动放行
     */
    private String autoapprove;
}

