package com.huiyu.auth.security.extension.refresh;

import cn.hutool.json.JSONObject;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.util.Assert;
import com.huiyu.auth.common.enums.AuthenticationIdentityEnum;
import com.huiyu.common.core.constant.SecurityConstants;
import com.huiyu.auth.security.config.Oauth2ServerConfig;
import com.huiyu.auth.security.core.userdetails.UserDetailsServiceImpl;
import com.huiyu.auth.util.RequestUtils;

import java.util.Map;

/**
 * 刷新token再次认证 UserDetailsService
 *
 * @author Naccl
 * @date 2022-03-08
 */
@NoArgsConstructor
public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
    /**
     * 客户端ID和用户服务 UserDetailService 的映射
     *
     * @see Oauth2ServerConfig#tokenServices(AuthorizationServerEndpointsConfigurer)
     */
    private Map<String, UserDetailsService> userDetailsServiceMap;

    public PreAuthenticatedUserDetailsService(Map<String, UserDetailsService> userDetailsServiceMap) {
        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
        this.userDetailsServiceMap = userDetailsServiceMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsServiceMap, "UserDetailsService must be set");
    }

    /**
     * 重写PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService 属性，可根据客户端和认证方式选择用户服务 UserDetailService 获取用户信息 UserDetail
     *
     * @param authentication
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        String clientId = RequestUtils.getOAuth2ClientId();
        // refresh_token的payload
        JSONObject payload = RequestUtils.getPayloadJSONObject();
        // 获取认证身份标识，默认是用户名:username
        AuthenticationIdentityEnum authenticationIdentityEnum = AuthenticationIdentityEnum.getByValue(RequestUtils.getAuthenticationIdentity(payload));
        UserDetailsService userDetailsService = userDetailsServiceMap.get(clientId);
        if (clientId.equals(SecurityConstants.WECHAT_CLIENT_ID)) {
            // 微信小程序认证方式是通过微信三方标识 openid 认证
            UserDetailsServiceImpl userDetailsServiceImpl = (UserDetailsServiceImpl) userDetailsService;
            switch (authenticationIdentityEnum) {
                case OPENID:
                    return userDetailsServiceImpl.loadUserByOpenId(RequestUtils.getOpenid(payload));
                default:
                    return userDetailsServiceImpl.loadUserByUsername(authentication.getName());
            }
        } else if (clientId.equals(SecurityConstants.WEB_ADMIN_CLIENT_ID)) {
            // web管理系统的认证方式通过用户名 username 认证
            switch (authenticationIdentityEnum) {
                default:
                    return userDetailsService.loadUserByUsername(authentication.getName());
            }
        } else {
            return userDetailsService.loadUserByUsername(authentication.getName());
        }
    }
}
