package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.core.entity.SysPermission;
import com.huiyu.service.core.model.query.SysPermissionQuery;

/**
 * (SysPermission)表服务接口
 *
 * @author Naccl
 * @date 2022-03-12
 */
public interface SysPermissionService extends IService<SysPermission> {
    /**
     * 分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<SysPermission> queryPage(IPage<SysPermission> page, SysPermissionQuery query);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysPermission queryById(Long id);

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
