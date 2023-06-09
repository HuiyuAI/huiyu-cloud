package com.huiyu.common.web.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.huiyu.common.core.constant.SecurityConstants;

/**
 * JWT工具类
 *
 * @author Naccl
 * @date 2022-03-11
 */
@Slf4j
public class JwtUtils {
    public static JSONObject getJwtPayload() {
        JSONObject jsonObject = null;
        String payload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(SecurityConstants.JWT_PAYLOAD_KEY);
        if (StrUtil.isNotBlank(payload)) {
            jsonObject = JSONUtil.parseObj(payload);
        }
        return jsonObject;
    }

    /**
     * 解析JWT获取用户id
     *
     * @return id
     */
    public static Long getId() {
        JSONObject payload = getJwtPayload();
        return payload == null ? null : payload.getLong("id");
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
