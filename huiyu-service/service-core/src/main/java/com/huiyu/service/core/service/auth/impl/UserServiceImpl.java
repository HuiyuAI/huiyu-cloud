package com.huiyu.service.core.service.auth.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.UserIdSender;
import com.huiyu.service.core.mapper.UserIdSenderMapper;
import com.huiyu.service.core.mapper.auth.UserMapper;
import com.huiyu.service.core.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (User)表服务实现类
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final UserIdSenderMapper userIdSenderMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long id) {
        return userMapper.queryById(id);
    }

    /**
     * 通过openid查询单条数据
     *
     * @param openid openid
     * @return 实例对象
     */
    @Override
    public User queryByOpenid(String openid) {
        return userMapper.queryByOpenid(openid);
    }

    /**
     * 通过username查询单条数据
     *
     * @param username username
     * @return 实例对象
     */
    @Override
    public User queryByUsername(String username) {
        return userMapper.queryByUsername(username);
    }

    /**
     * 通过userId查询单条数据
     *
     * @param userId userId
     * @return 实例对象
     */
    @Override
    public User queryByUserId(Long userId) {
        return userMapper.queryByUserId(userId);
    }

    /**
     * 统计总行数
     *
     * @param user 查询条件
     * @return 总行数
     */
    @Override
    public long count(User user) {
        return super.lambdaQuery(user).count();
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User insert(User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 从发号表取一个userId
        UserIdSender userIdSender = userIdSenderMapper.getFirst();
        user.setUserId(userIdSender.getUserId());
        userIdSenderMapper.deleteById(userIdSender.getId());
        super.save(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return super.updateById(user);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public int getIntegralById(Long id) {
        return userMapper.getIntegralById(id);
    }

    @Override
    public boolean updateIntegralById(Long id, Integer integral) {
        return userMapper.updateIntegralById(id, integral) > 0;
    }
}
