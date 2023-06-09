package com.huiyu.common.core.constant;

/**
 * 权限相关常量
 *
 * @author Naccl
 * @date 2022-03-08
 */
public class SecurityConstants {
    /**
     * 认证请求头key
     */
    public static final String AUTHORIZATION_KEY = "Authorization";
    /**
     * JWT令牌前缀Bearer
     */
    public static final String JWT_PREFIX = "Bearer ";
    /**
     * JWT载体key-解析到header中的key
     */
    public static final String JWT_PAYLOAD_KEY = "payload";
    /**
     * 角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";
    /**
     * JWT存储权限属性的key
     */
    public static final String AUTHORITY_CLAIM_NAME = "role";
    /**
     * 角色-ROOT
     */
    public static final String ROLE_ROOT = "ROOT";
    /**
     * 角色-ADMIN
     */
    public static final String ROLE_ADMIN = "ADMIN";
    /**
     * 角色-USER
     */
    public static final String ROLE_USER = "USER";
    /**
     * 客户端id key
     */
    public static final String CLIENT_ID_KEY = "client_id";
    /**
     * 刷新token key
     */
    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    /**
     * 认证身份标识 key
     */
    public static final String AUTHENTICATION_IDENTITY_KEY = "authenticationIdentity";
    /**
     * openid key
     */
    public static final String OPENID = "openid";
    /**
     * 系统管理web客户端ID
     */
    public static final String WEB_ADMIN_CLIENT_ID = "web-admin";
    /**
     * 微信小程序客户端ID
     */
    public static final String WECHAT_CLIENT_ID = "wechat";
}
