package com.huiyu.auth.security.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.huiyu.service.api.feign.UserFeignClient;
import com.huiyu.auth.security.extension.wechat.WechatAuthenticationProvider;
import com.huiyu.auth.security.core.userdetails.UserDetailsServiceImpl;

/**
 * SpringSecurity配置
 *
 * @author Naccl
 * @date 2022-03-01
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserFeignClient userFeignClient;
    private final UserDetailsServiceImpl userDetailsService;
    private final WxMaService wxMaService;
    @Value("${huiyu.default-avatar}")
    private String defaultAvatar;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers("/rsa/publicKey").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(wechatAuthenticationProvider())
                .authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 微信认证授权提供者
     *
     * @return
     */
    @Bean
    public WechatAuthenticationProvider wechatAuthenticationProvider() {
        return new WechatAuthenticationProvider(userFeignClient, userDetailsService, wxMaService, defaultAvatar);
    }

    /**
     * 用户名密码认证授权提供者
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * 密文编码器
     * 委托方式，根据密文的前缀选择对应的encoder，例如：{bcypt}前缀->标识BCYPT算法加密；{noop}->标识不使用任何加密即明文的方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
