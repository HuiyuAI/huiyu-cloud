package com.huiyu.service.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.api.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysPermission)表数据库访问层
 *
 * @author Naccl
 * @date 2022-03-12
 */
@Mapper
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 查询指定行数据
     *
     * @param sysPermission 查询条件
     * @return 对象列表
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
     * @param sysPermission 查询条件
     * @return 总行数
     */
    long count(SysPermission sysPermission);

    /**
     * 新增数据
     *
     * @param sysPermission 实例对象
     * @return 影响行数
     */
    int insert(SysPermission sysPermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysPermission> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPermission> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysPermission> entities);

    /**
     * 修改数据
     *
     * @param sysPermission 实例对象
     * @return 影响行数
     */
    int update(SysPermission sysPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);
}
