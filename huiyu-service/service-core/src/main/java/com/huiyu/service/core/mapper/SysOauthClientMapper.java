package com.huiyu.service.core.mapper;

import com.huiyu.service.api.entity.SysOauthClient;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysOauthClient)表数据库访问层
 *
 * @author Naccl
 * @date 2022-03-07
 */
@Mapper
@Repository
public interface SysOauthClientMapper {
    /**
     * 查询指定行数据
     *
     * @param sysOauthClient 查询条件
     * @return 对象列表
     */
    List<SysOauthClient> queryAll(SysOauthClient sysOauthClient);

    /**
     * 通过主键查询单条数据
     *
     * @param clientId 主键
     * @return 实例对象
     */
    SysOauthClient queryByClientId(String clientId);

    /**
     * 统计总行数
     *
     * @param sysOauthClient 查询条件
     * @return 总行数
     */
    long count(SysOauthClient sysOauthClient);

    /**
     * 新增数据
     *
     * @param sysOauthClient 实例对象
     * @return 影响行数
     */
    int insert(SysOauthClient sysOauthClient);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysOauthClient> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysOauthClient> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysOauthClient> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysOauthClient> entities);

    /**
     * 修改数据
     *
     * @param sysOauthClient 实例对象
     * @return 影响行数
     */
    int update(SysOauthClient sysOauthClient);

    /**
     * 通过主键删除数据
     *
     * @param clientId 主键
     * @return 影响行数
     */
    int deleteByClientId(String clientId);
}
