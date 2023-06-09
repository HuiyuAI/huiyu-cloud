package com.huiyu.auth.security.extension.wechat;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微信授权者
 *
 * @author Naccl
 * @date 2022-03-08
 */
public class WechatTokenGranter extends AbstractTokenGranter {
    /**
     * 声明授权者 CaptchaTokenGranter 支持授权模式 wechat
     * 根据接口传值 grant_type = wechat 的值匹配到此授权者
     * 匹配逻辑详见下面的两个方法
     *
     * @see org.springframework.security.oauth2.provider.CompositeTokenGranter#grant(String, TokenRequest)
     * @see AbstractTokenGranter#grant(String, TokenRequest)
     */
    private static final String GRANT_TYPE = "wechat";
    private final AuthenticationManager authenticationManager;

    public WechatTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
        String code = parameters.get("code");
        String encryptedData = parameters.get("encryptedData");
        String iv = parameters.get("iv");

        // 移除后续无用参数
        parameters.remove("code");
        parameters.remove("encryptedData");
        parameters.remove("iv");

        // 未认证状态
        Authentication userAuth = new WechatAuthenticationToken(code, encryptedData, iv);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        try {
            // 认证中
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (Exception e) {
            throw new InvalidGrantException(e.getMessage());
        }

        // 认证成功
        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            // 认证失败
            throw new InvalidGrantException("Could not authenticate code: " + code);
        }
    }
}
