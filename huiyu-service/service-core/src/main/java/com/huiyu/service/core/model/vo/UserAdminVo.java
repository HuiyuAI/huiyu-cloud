package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户表(User)后台管理视图类
 *
 * @author Naccl
 * @date 2023-07-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminVo {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 微信小程序openid
     */
    private String openid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 角色
     */
    private String role;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 每日积分
     */
    private Integer dailyPoint;
    /**
     * 积分
     */
    private Integer point;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 状态
     */
    private Boolean enabled;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 图片数量
     */
    private Integer picCount;
}
