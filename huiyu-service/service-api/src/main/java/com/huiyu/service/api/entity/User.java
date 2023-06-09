package com.huiyu.service.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (User)实体类
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    /**
     * 手机号
     */
    private String phone;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    private Integer isDelete;
}
