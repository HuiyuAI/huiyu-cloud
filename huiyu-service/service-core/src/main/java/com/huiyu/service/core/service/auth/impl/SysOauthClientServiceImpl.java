package com.huiyu.service.core.service.auth.impl;

import com.huiyu.service.api.entity.SysOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import com.huiyu.service.core.mapper.auth.SysOauthClientMapper;
import com.huiyu.service.core.service.auth.SysOauthClientService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (SysOauthClient)表服务实现类
 *
 * @author Naccl
 * @date 2022-03-07
 */
@RequiredArgsConstructor
@Service
public class SysOauthClientServiceImpl implements SysOauthClientService {
    private final SysOauthClientMapper sysOauthClientMapper;

    /**
     * 分页查询
     *
     * @param sysOauthClient 筛选条件
     * @return 查询结果
     */
    @Override
    public List<SysOauthClient> queryAll(SysOauthClient sysOauthClient) {
        return sysOauthClientMapper.queryAll(sysOauthClient);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param clientId 主键
     * @return 实例对象
     */
    @Override
    public SysOauthClient queryByClientId(String clientId) {
        return sysOauthClientMapper.queryByClientId(clientId);
    }

    /**
     * 统计总行数
     *
     * @param sysOauthClient 筛选条件
     * @return 总行数
     */
    @Override
    public long count(SysOauthClient sysOauthClient) {
        return sysOauthClientMapper.count(sysOauthClient);
    }

    /**
     * 新增数据
     *
     * @param sysOauthClient 实例对象
     * @return 实例对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysOauthClient insert(SysOauthClient sysOauthClient) {
        sysOauthClientMapper.insert(sysOauthClient);
        return sysOauthClient;
    }

    /**
     * 修改数据
     *
     * @param sysOauthClient 实例对象
     * @return 实例对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SysOauthClient sysOauthClient) {
        return sysOauthClientMapper.update(sysOauthClient) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param clientId 主键
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByClientId(String clientId) {
        return sysOauthClientMapper.deleteByClientId(clientId) > 0;
    }
}
