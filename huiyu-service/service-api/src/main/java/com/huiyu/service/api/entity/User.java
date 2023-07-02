package com.huiyu.service.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 微信小程序openid
     */
    @TableField("openid")
    private String openid;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 角色
     */
    @TableField("role")
    private String role;
    /**
     * 头像url
     */
    @TableField("avatar")
    private String avatar;
    /**
     * 性别
     */
    @TableField("gender")
    private Integer gender;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 积分
     */
    @TableField("integral")
    private Integer integral;
    /**
     * 等级
     */
    @TableField("level")
    private Integer level;
    /**
     * 状态
     */
    @TableField("enabled")
    private Boolean enabled;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;
}
