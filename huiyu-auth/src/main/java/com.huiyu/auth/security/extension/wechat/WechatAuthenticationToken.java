package com.huiyu.auth.security.extension.wechat;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 微信身份验证token
 *
 * @author Naccl
 * @date 2022-03-08
 */
public class WechatAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;

    /**
     * 账号校验之前的token构建
     *
     * @param principal
     */
    public WechatAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        setAuthenticated(false);
    }

    /**
     * 账号校验成功之后的token构建
     *
     * @param principal
     * @param authorities
     */
    public WechatAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(isAuthenticated == false, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
