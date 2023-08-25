package com.huiyu.auth.security.extension.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.RandomUtil;
import com.huiyu.common.core.util.JacksonUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import com.huiyu.common.core.constant.SecurityConstants;
import com.huiyu.auth.security.core.userdetails.UserDetailsServiceImpl;
import com.huiyu.service.api.feign.UserFeignClient;
import com.huiyu.service.api.entity.User;
import com.huiyu.common.core.result.R;

import java.util.HashSet;

/**
 * 微信认证提供者
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Slf4j
@Setter
public class WechatAuthenticationProvider implements AuthenticationProvider {
    private UserFeignClient userFeignClient;
    private UserDetailsServiceImpl userDetailsService;
    private WxMaService wxMaService;

    /**
     * 微信认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();

        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            log.info("请求微信sessionInfo: {}", JacksonUtils.toJsonStr(sessionInfo));
        } catch (WxErrorException e) {
            log.error("请求微信sessionInfo error: {}", e);
        }
        String openid = sessionInfo.getOpenid();

        R<User> result = userFeignClient.queryByOpenid(openid);
        log.info("根据openid查询用户结果: {}", JacksonUtils.toJsonStr(result));
        User user = result.getData();
        // 不管有没有查到用户，状态码总是SUCCESS
        if (result.isSuccess() && user == null) {
            User newUser = new User();
            newUser.setOpenid(openid);
            newUser.setNickname("绘画师" + RandomUtil.randomNumbers(5));
            newUser.setAvatar("https://huiyucdn.naccl.top/upload/avatar/96d740cc-06f3-403c-96d4-6ce29d6a5e9a.jpg");
            newUser.setGender(0);
            newUser.setEnabled(true);
            newUser.setRole(SecurityConstants.ROLE_PREFIX + SecurityConstants.ROLE_USER);
            log.info("微信小程序注册新用户: {}", JacksonUtils.toJsonStr(newUser));
            // 注册新用户
            R<User> addResult = userFeignClient.add(newUser);
            log.info("微信小程序注册新用户结果: {}", JacksonUtils.toJsonStr(addResult));
        }
        UserDetails userDetails = userDetailsService.loadUserByOpenId(openid);
        WechatAuthenticationToken wechatAuthenticationToken = new WechatAuthenticationToken(userDetails, new HashSet<>());
        wechatAuthenticationToken.setDetails(authentication.getDetails());
        return wechatAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
