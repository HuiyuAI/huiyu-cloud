package com.huiyu.service.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.api.entity.SysOauthClient;
import com.huiyu.service.core.model.query.SysOauthClientQuery;

/**
 * (SysOauthClient)表服务接口
 *
 * @author Naccl
 * @date 2022-03-07
 */
public interface SysOauthClientService extends IService<SysOauthClient> {
    /**
     * 分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    IPage<SysOauthClient> queryPage(IPage<SysOauthClient> page, SysOauthClientQuery query);

    /**
     * 通过主键查询单条数据
     *
     * @param clientId 主键
     * @return 实例对象
     */
    SysOauthClient queryByClientId(String clientId);

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
