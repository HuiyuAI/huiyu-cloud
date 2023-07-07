package com.huiyu.service.core.service.auth.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.core.entity.SysPermission;
import com.huiyu.service.core.model.query.SysPermissionQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.huiyu.common.redis.constant.RedisKeyConstant;
import com.huiyu.service.core.mapper.auth.SysPermissionMapper;
import com.huiyu.service.core.service.auth.SysPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (SysPermission)表服务实现类
 *
 * @author Naccl
 * @date 2022-03-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    private final SysPermissionMapper sysPermissionMapper;
    private final RedisTemplate redisTemplate;

    @PostConstruct
    public void initPermissionRolesCache() {
        boolean result = this.refreshPermRolesRules();
        log.info("初始化权限角色规则缓存: {}", result);
    }

    /**
     * 分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    @Override
    public IPage<SysPermission> queryPage(IPage<SysPermission> page, SysPermissionQuery query) {
        return super.baseMapper.queryPage(page, query);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysPermission queryById(Long id) {
        return sysPermissionMapper.queryById(id);
    }


    /**
     * 统计总行数
     *
     * @param sysPermission 筛选条件
     * @return 总行数
     */
    @Override
    public long count(SysPermission sysPermission) {
        return sysPermissionMapper.count(sysPermission);
    }

    /**
     * 新增数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPermission insert(SysPermission sysPermission) {
        sysPermissionMapper.insert(sysPermission);
        return sysPermission;
    }

    /**
     * 修改数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SysPermission sysPermission) {
        return sysPermissionMapper.update(sysPermission) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteById(Long id) {
        return sysPermissionMapper.deleteById(id) > 0;
    }

    /**
     * 刷新权限角色缓存
     *
     * @return 是否成功
     */
    @Override
    public boolean refreshPermRolesRules() {
        redisTemplate.delete(RedisKeyConstant.RESOURCE_ROLES_MAP);
        List<SysPermission> sysPermissions = sysPermissionMapper.queryAll(new SysPermission());
        if (CollUtil.isNotEmpty(sysPermissions)) {
            // 初始化URL【权限->角色(集合)】规则
            List<SysPermission> urlPermList = sysPermissions.stream()
                    .filter(item -> StrUtil.isNotBlank(item.getUrlPerm()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(urlPermList)) {
                Map<String, String> urlPermRoles = MapUtil.newHashMap(urlPermList.size());
                urlPermList.forEach(item -> {
                    String urlPerm = item.getUrlPerm();
                    String roles = item.getRole();
                    urlPermRoles.put(urlPerm, roles);
                    log.info("urlPerm:{}, roles:{}", urlPerm, roles);
                });
                redisTemplate.opsForHash().putAll(RedisKeyConstant.RESOURCE_ROLES_MAP, urlPermRoles);
                return true;
            }
        }
        // 规则为空，则添加一个空规则
        redisTemplate.opsForHash().put(RedisKeyConstant.RESOURCE_ROLES_MAP, "placeholder", "['null']");
        log.info("角色权限规则为空");
        return true;
    }
}
