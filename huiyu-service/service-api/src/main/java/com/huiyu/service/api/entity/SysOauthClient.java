package com.huiyu.service.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_oauth_client")
public class SysOauthClient implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 客户端ID
     */
    @TableId("client_id")
    private String clientId;
    /**
     * 资源id列表
     */
    @TableField("resource_ids")
    private String resourceIds;
    /**
     * 客户端密钥
     */
    @TableField("client_secret")
    private String clientSecret;
    /**
     * 域
     */
    @TableField("scope")
    private String scope;
    /**
     * 授权方式
     */
    @TableField("authorized_grant_types")
    private String authorizedGrantTypes;
    /**
     * 回调地址
     */
    @TableField("web_server_redirect_uri")
    private String webServerRedirectUri;
    /**
     * 权限列表
     */
    @TableField("authorities")
    private String authorities;
    /**
     * 认证令牌时效
     */
    @TableField("access_token_validity")
    private Integer accessTokenValidity;
    /**
     * 刷新令牌时效
     */
    @TableField("refresh_token_validity")
    private Integer refreshTokenValidity;
    /**
     * 扩展信息
     */
    @TableField("additional_information")
    private String additionalInformation;
    /**
     * 是否自动放行
     */
    @TableField("autoapprove")
    private String autoapprove;
}

