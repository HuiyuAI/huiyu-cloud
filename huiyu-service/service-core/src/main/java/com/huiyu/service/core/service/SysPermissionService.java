package com.huiyu.service.core.service;

import com.huiyu.service.api.entity.SysPermission;

import java.util.List;

/**
 * (SysPermission)表服务接口
 *
 * @author Naccl
 * @date 2022-03-12
 */
public interface SysPermissionService {
    /**
     * 分页查询
     *
     * @param sysPermission 筛选条件
     * @return 查询结果
     */
    List<SysPermission> queryAll(SysPermission sysPermission);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysPermission queryById(Long id);

    /**
     * 统计总行数
     *
     * @param sysPermission 筛选条件
     * @return 总行数
     */
    long count(SysPermission sysPermission);

    /**
     * 新增数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    SysPermission insert(SysPermission sysPermission);

    /**
     * 修改数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    boolean update(SysPermission sysPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 加载角色权限规则至Redis缓存
     *
     * @return 是否成功
     */
    boolean refreshPermRolesRules();
}
