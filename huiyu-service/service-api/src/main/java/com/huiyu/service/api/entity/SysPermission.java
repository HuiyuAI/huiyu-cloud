package com.huiyu.service.api.entity;

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
public class SysPermission implements Serializable {
    private Long id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * URL权限标识
     */
    private String urlPerm;
    /**
     * 角色
     */
    private String role;
    /**
     * 排序，从0开始，越小越靠前
     */
    private Long orderId;
}
