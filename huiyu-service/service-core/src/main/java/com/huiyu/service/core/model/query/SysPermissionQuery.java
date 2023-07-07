package com.huiyu.service.core.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysPermission)查询对象实体类
 *
 * @author Naccl
 * @date 2023-07-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPermissionQuery {

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
}
