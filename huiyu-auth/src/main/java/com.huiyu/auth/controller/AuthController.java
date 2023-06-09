package com.huiyu.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.huiyu.common.core.result.R;

import java.security.Principal;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 *
 * @author Naccl
 * @date 2022-03-01
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class AuthController {
    private final TokenEndpoint tokenEndpoint;

    /**
     * Oauth2登录认证
     */
    @PostMapping("/token")
    public R<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return R.ok(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }
}
