package com.huiyu.auth.domain;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 登录用户信息
 *
 * @author Naccl
 * @date 2022-03-07
 */
@Data
public class SecurityUserDetails implements UserDetails {
    /**
     * id
     */
    private Long id;
    /**
     * openid
     */
    private String openid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 状态
     */
    private Boolean enabled;
    /**
     * 角色
     */
    private String role;
    /**
     * 扩展字段：认证身份标识，枚举值如下：
     *
     * @see com.huiyu.auth.common.enums.AuthenticationIdentityEnum
     */
    private String authenticationIdentity;

    public SecurityUserDetails(User user) {
        this.id = user.getId();
        this.openid = user.getOpenid();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.avatar = user.getAvatar();
        this.gender = user.getGender();
        this.enabled = user.getEnabled();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CollUtil.toList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
