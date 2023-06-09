package com.huiyu.auth.security.extension.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
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
            log.info("wechat sessionInfo={}", JSONUtil.toJsonStr(sessionInfo));
        } catch (WxErrorException e) {
            log.error("请求微信sessionInfo error: {}", e);
            e.printStackTrace();
        }
        String openid = sessionInfo.getOpenid();

        R<User> result = userFeignClient.queryByOpenid(openid);
        log.info("authenticate result={}", JSONUtil.toJsonStr(result));
        User user = result.getData();
        // 不管有没有查到用户，状态码总是SUCCESS
        if (result.isSuccess() && user == null) {
            String sessionKey = sessionInfo.getSessionKey();
            String encryptedData = authenticationToken.getEncryptedData();
            String iv = authenticationToken.getIv();
            // 解密 encryptedData 获取用户信息
            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            log.info("userInfo={}", JSONUtil.toJsonStr(userInfo));

            User newUser = new User();
            newUser.setOpenid(openid);
            newUser.setUsername(getUniqueUsername(userInfo.getNickName()));
            newUser.setAvatar(userInfo.getAvatarUrl());
            newUser.setGender(Integer.parseInt(userInfo.getGender()));
            newUser.setEnabled(true);
            newUser.setRole(SecurityConstants.ROLE_PREFIX + SecurityConstants.ROLE_USER);
            log.info("newUser={}", JSONUtil.toJsonStr(newUser));
            // 注册新用户
            R<User> addResult = userFeignClient.add(newUser);
            log.info("addResult={}", JSONUtil.toJsonStr(addResult));
        }
        UserDetails userDetails = userDetailsService.loadUserByOpenId(openid);
        WechatAuthenticationToken wechatAuthenticationToken = new WechatAuthenticationToken(userDetails, new HashSet<>());
        wechatAuthenticationToken.setDetails(authentication.getDetails());
        return wechatAuthenticationToken;
    }

    /**
     * TODO 现在username是唯一的，后面要把loadUserByUsername()改掉
     * 获取唯一用户名
     *
     * @param nickname 原始昵称
     * @return 唯一用户名
     */
    private String getUniqueUsername(String nickname) {
        // 检查用户名是否存在
        R<Long> countResult = userFeignClient.count(User.builder().username(nickname).build());
        if (countResult.isSuccess() && countResult.getData() > 0) {
            // 用户名已存在，拼上一个随机数
            nickname = nickname + RandomUtil.randomNumber();
            // 继续检查是否存在
            return getUniqueUsername(nickname);
        }
        return nickname;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
