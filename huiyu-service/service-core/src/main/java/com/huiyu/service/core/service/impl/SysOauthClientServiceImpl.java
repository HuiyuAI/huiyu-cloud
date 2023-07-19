package com.huiyu.service.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.api.entity.SysOauthClient;
import com.huiyu.service.core.model.query.SysOauthClientQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.huiyu.service.core.mapper.auth.SysOauthClientMapper;
import com.huiyu.service.core.service.SysOauthClientService;
import org.springframework.stereotype.Service;

/**
 * (SysOauthClient)表服务实现类
 *
 * @author Naccl
 * @date 2022-03-07
 */
@RequiredArgsConstructor
@Service
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient> implements SysOauthClientService {
    private final SysOauthClientMapper sysOauthClientMapper;

    /**
     * 分页查询
     *
     * @param page  分页对象
     * @param query 筛选条件
     * @return 查询结果
     */
    @Override
    public IPage<SysOauthClient> queryPage(IPage<SysOauthClient> page, SysOauthClientQuery query) {
        return super.lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getClientId()), SysOauthClient::getClientId, query.getClientId())
                .page(page);
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
