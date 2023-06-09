package com.huiyu.service.core.service;

import com.huiyu.service.api.entity.SysOauthClient;

import java.util.List;

/**
 * (SysOauthClient)表服务接口
 *
 * @author Naccl
 * @date 2022-03-07
 */
public interface SysOauthClientService {
    /**
     * 分页查询
     *
     * @param sysOauthClient 筛选条件
     * @return 查询结果
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
     * @param sysOauthClient 筛选条件
     * @return 总行数
     */
    long count(SysOauthClient sysOauthClient);

    /**
     * 新增数据
     *
     * @param sysOauthClient 实例对象
     * @return 实例对象
     */
    SysOauthClient insert(SysOauthClient sysOauthClient);

    /**
     * 修改数据
     *
     * @param sysOauthClient 实例对象
     * @return 实例对象
     */
    boolean update(SysOauthClient sysOauthClient);

    /**
     * 通过主键删除数据
     *
     * @param clientId 主键
     * @return 是否成功
     */
    boolean deleteByClientId(String clientId);
}
