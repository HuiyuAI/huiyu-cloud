package com.huiyu.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User implements Serializable {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 微信小程序openid
     */
    private String openid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 状态
     */
    private Boolean enabled;
    /**
     * 角色
     */
    private String role;
}
