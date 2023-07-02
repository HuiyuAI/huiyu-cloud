package com.huiyu.auth.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.huiyu.auth.common.enums.AuthenticationIdentityEnum;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.huiyu.common.core.constant.SecurityConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Slf4j
public class RequestUtils {
    /**
     * 获取OAuth2客户端client_id
     *
     * @return client_id
     */
    public static String getOAuth2ClientId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从请求路径中获取
        return request.getParameter(SecurityConstants.CLIENT_ID_KEY);
    }

    /**
     * 解析refresh_token获取payload
     *
     * @return payload
     */
    @SneakyThrows
    public static JSONObject getPayloadJSONObject() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String refreshToken = request.getParameter(SecurityConstants.REFRESH_TOKEN_KEY);
        String payload = StrUtil.toString(JWSObject.parse(refreshToken).getPayload());
        return JSONUtil.parseObj(payload);
    }

    /**
     * 获取认证身份标识
     *
     * @param payload token payload
     * @return 认证身份标识
     */
    public static String getAuthenticationIdentity(JSONObject payload) {
        if (payload == null) {
            payload = getPayloadJSONObject();
        }
        return payload.getStr(SecurityConstants.AUTHENTICATION_IDENTITY_KEY);
    }

    /**
     * 获取openid
     *
     * @param payload token payload
     * @return openid
     */
    public static String getOpenid(JSONObject payload) {
        if (payload == null) {
            payload = getPayloadJSONObject();
        }
        return payload.getStr(AuthenticationIdentityEnum.OPENID.getValue());
    }
}
