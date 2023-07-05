package com.huiyu.common.web.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.huiyu.common.core.constant.SecurityConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * JWT工具类
 *
 * @author Naccl
 * @date 2022-03-11
 */
@Slf4j
public class JwtUtils {
    private static final String HUIYU_USER_REQUEST_ATTR = "_HUIYU_USER_REQUEST_ATTR_";

    public static JSONObject getJwtPayload() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object huiyuUser = request.getAttribute(HUIYU_USER_REQUEST_ATTR);

        if (huiyuUser == null) {
            String payload = request.getHeader(SecurityConstants.JWT_PAYLOAD_KEY);
            if (StrUtil.isNotBlank(payload)) {
                huiyuUser = JSONUtil.parseObj(payload);
                request.setAttribute(HUIYU_USER_REQUEST_ATTR, huiyuUser);
            }
        }
        return (JSONObject) huiyuUser;
    }

    /**
     * 解析JWT获取用户id
     *
     * @return id
     */
    public static Long getUserId() {
        JSONObject payload = getJwtPayload();
        return payload == null ? null : payload.getLong("userId");
    }

    /**
     * 解析JWT获取用户openid
     *
     * @return openid
     */
    public static String getOpenid() {
        JSONObject payload = getJwtPayload();
        return payload == null ? null : payload.getStr("openid");
    }

    /**
     * 解析JWT获取获取username
     *
     * @return username
     */
    public static String getUsername() {
        JSONObject payload = getJwtPayload();
        return payload == null ? null : payload.getStr("username");
    }

    /**
     * 解析JWT获取获取role
     *
     * @return role
     */
    public static String getRole() {
        JSONObject payload = getJwtPayload();
        return payload == null ? null : payload.getStr("role");
    }

    /**
     * 是否「超级管理员」
     *
     * @return
     */
    public static boolean isRoot() {
        String role = getRole();
        return (SecurityConstants.ROLE_PREFIX + SecurityConstants.ROLE_ROOT).equals(role);
    }
}
