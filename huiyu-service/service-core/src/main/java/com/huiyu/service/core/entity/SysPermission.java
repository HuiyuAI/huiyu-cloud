package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysPermission)实体类
 *
 * @author Naccl
 * @date 2022-03-12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_permission")
public class SysPermission implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId("id")
    private Long id;
    /**
     * 权限名称
     */
    @TableField("name")
    private String name;
    /**
     * URL权限标识
     */
    @TableField("url_perm")
    private String urlPerm;
    /**
     * 角色
     */
    @TableField("role")
    private String role;
    /**
     * 排序，从0开始，越小越靠前
     */
    @TableField("order_id")
    private Long orderId;
}
