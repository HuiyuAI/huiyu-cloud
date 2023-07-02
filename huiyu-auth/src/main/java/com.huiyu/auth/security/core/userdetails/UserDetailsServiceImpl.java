package com.huiyu.auth.security.core.userdetails;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.feign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.huiyu.auth.common.enums.AuthenticationIdentityEnum;
import com.huiyu.auth.common.constant.MessageConstant;
import com.huiyu.auth.domain.SecurityUserDetails;
import com.huiyu.common.core.constant.SecurityConstants;
import com.huiyu.service.api.entity.User;
import com.huiyu.common.core.result.R;

/**
 * 用户信息
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserFeignClient userFeignClient;

    /**
     * username 认证方式
     *
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        R<User> result = userFeignClient.queryByUsername(username);
        User user = result.getData();
        // 用户名密码认证方式不允许 ROLE_USER 角色登录
        if (result.isSuccess()
                && user != null
                && !(SecurityConstants.ROLE_PREFIX + SecurityConstants.ROLE_USER).equalsIgnoreCase(user.getRole())) {
            SecurityUserDetails userDetails = new SecurityUserDetails(user);
            log.info("loadUserByUsername userDetails: {}", JacksonUtils.toJsonStr(userDetails));
            // 认证方式:username
            userDetails.setAuthenticationIdentity(AuthenticationIdentityEnum.USERNAME.getValue());
            checkUserDetails(userDetails);
            return userDetails;
        }
        throw new UsernameNotFoundException(MessageConstant.USER_NOT_FOUND);
    }

    /**
     * openid 认证方式
     *
     * @param openid
     * @return
     */
    public UserDetails loadUserByOpenId(String openid) {
        R<User> result = userFeignClient.queryByOpenid(openid);
        User user = result.getData();
        if (result.isSuccess() && user != null) {
            SecurityUserDetails userDetails = new SecurityUserDetails(user);
            log.info("loadUserByOpenId userDetails: {}", JacksonUtils.toJsonStr(userDetails));
            // 认证方式:openid
            userDetails.setAuthenticationIdentity(AuthenticationIdentityEnum.OPENID.getValue());
            checkUserDetails(userDetails);
            return userDetails;
        }
        throw new UsernameNotFoundException(MessageConstant.USER_NOT_FOUND);
    }

    /**
     * 校验账户状态
     *
     * @param userDetails
     */
    private void checkUserDetails(SecurityUserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!userDetails.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
    }
}
