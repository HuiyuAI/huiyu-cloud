package com.huiyu.service.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.service.core.entity.SysPermission;
import com.huiyu.service.core.model.query.SysPermissionQuery;
import com.huiyu.service.core.service.auth.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huiyu.common.core.result.R;

/**
 * (SysPermission)表控制层
 *
 * @author Naccl
 * @date 2022-03-12
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sysPermission")
public class SysPermissionController {
    private final SysPermissionService sysPermissionService;

    /**
     * 分页查询
     *
     * @param query    筛选条件
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return 查询结果
     */
    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<SysPermission>> queryByPage(SysPermissionQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<SysPermission> pageInfo = sysPermissionService.queryPage(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R<SysPermission> queryById(@PathVariable("id") Long id) {
        return R.ok(sysPermissionService.queryById(id));
    }

    /**
     * 统计总行数
     *
     * @param sysPermission 筛选条件
     * @return 总行数
     */
    @GetMapping("/count")
    public R<Long> count(SysPermission sysPermission) {
        return R.ok(sysPermissionService.count(sysPermission));
    }

    /**
     * 新增数据
     *
     * @param sysPermission 实体
     * @return 新增结果
     */
    @PostMapping
    public R<SysPermission> add(@RequestBody SysPermission sysPermission) {
        return R.ok(sysPermissionService.insert(sysPermission));
    }

    /**
     * 编辑数据
     *
     * @param sysPermission 实体
     * @return 编辑结果
     */
    @PutMapping
    public <T> R<T> edit(@RequestBody SysPermission sysPermission) {
        sysPermissionService.update(sysPermission);
        return R.ok();
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public <T> R<T> deleteById(@PathVariable("id") Long id) {
        sysPermissionService.deleteById(id);
        return R.ok();
    }

    /**
     * 加载角色权限规则至Redis缓存
     *
     * @return 是否成功
     */
    @PutMapping("/refreshPermRolesRules")
    public R<Boolean> refreshPermRolesRules() {
        return R.ok(sysPermissionService.refreshPermRolesRules());
    }
}
