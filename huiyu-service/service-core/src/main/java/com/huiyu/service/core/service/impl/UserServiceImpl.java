package com.huiyu.service.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.entity.UserIdSender;
import com.huiyu.common.web.exception.BizException;
import com.huiyu.service.core.mapper.UserIdSenderMapper;
import com.huiyu.service.core.mapper.auth.UserMapper;
import com.huiyu.service.core.model.query.UserQuery;
import com.huiyu.service.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Override
    public IPage<User> adminPageQuery(IPage<User> page, UserQuery query) {
        return super.lambdaQuery()
                .eq(query.getUserId() != null, User::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getRole()), User::getRole, query.getRole())
                .ge(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, User::getCreateTime, query.getCreateTimeStart())
                .le(query.getCreateTimeStart() != null && query.getCreateTimeEnd() != null, User::getCreateTime, query.getCreateTimeEnd())
                .orderByDesc(User::getId)
                .page(page);
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

        // 上次更新时间
        LocalDateTime lastUpdateTime = user.getUpdateTime();

        user.setUpdateTime(LocalDateTime.now());
        boolean isUpdateUserOK = super.lambdaUpdate()
                .eq(User::getUserId, user.getUserId())
                // 判断更新时间是否相同，防止并发更新
                .eq(User::getUpdateTime, lastUpdateTime)
                .update(user);
        if (!isUpdateUserOK) {
            log.error("编辑用户信息失败，user: {}", JacksonUtils.toJsonStr(user));
            throw new BizException("编辑用户信息失败！用户信息已被其它操作修改，请刷新后重试！");
        }
        return true;
    }

    @Override
    public int getPointByUserId(Long userId) {
        return userMapper.getPointByUserId(userId);
    }

    @Override
    public boolean updatePointByUserId(Long userId, Integer dailyPoint, Integer point) {
        return userMapper.updatePointByUserId(userId, dailyPoint, point) > 0;
    }

    @Override
    public boolean updateAvatar(Long userId, String avatar) {
        return super.lambdaUpdate()
                .eq(User::getUserId, userId)
                .set(User::getAvatar, avatar)
                .update();
    }

    @Override
    public boolean updateNickname(Long userId, String nickname) {
        return super.lambdaUpdate()
                .eq(User::getUserId, userId)
                .set(User::getNickname, nickname)
                .update();
    }
}
