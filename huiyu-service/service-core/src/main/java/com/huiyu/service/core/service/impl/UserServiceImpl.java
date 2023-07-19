package com.huiyu.service.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.convert.UserConvert;
import com.huiyu.service.core.entity.UserIdSender;
import com.huiyu.service.core.mapper.UserIdSenderMapper;
import com.huiyu.service.core.mapper.auth.UserMapper;
import com.huiyu.service.core.model.dto.UserPicCountDto;
import com.huiyu.service.core.model.query.UserQuery;
import com.huiyu.service.core.model.vo.UserAdminVo;
import com.huiyu.service.core.service.PicService;
import com.huiyu.service.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final PicService picService;

    @Override
    public IPage<UserAdminVo> adminPageQuery(IPage<User> page, UserQuery query) {
        IPage<User> userPage = super.lambdaQuery()
                .eq(query.getUserId() != null, User::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getRole()), User::getRole, query.getRole())
                .page(page);

        IPage<UserAdminVo> userAdminVoPage = UserConvert.INSTANCE.toVOPage(userPage);
        if (CollectionUtils.isEmpty(userPage.getRecords())) {
            return userAdminVoPage;
        }

        List<Long> userIdList = userAdminVoPage.getRecords().stream().map(UserAdminVo::getUserId).collect(Collectors.toList());
        List<UserPicCountDto> userPicCountDtoList = picService.countByUserIdList(userIdList);
        Map<Long, Integer> userPicCountMap = userPicCountDtoList.stream().collect(Collectors.toMap(UserPicCountDto::getUserId, UserPicCountDto::getPicCount, (k1, k2) -> k1));

        userAdminVoPage.getRecords().forEach(userAdminVo -> {
            Integer count = userPicCountMap.get(userAdminVo.getUserId());
            userAdminVo.setPicCount(count == null ? 0 : count);
        });
        return userAdminVoPage;
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

    @Override
    public int getPointByUserId(Long userId) {
        return userMapper.getPointByUserId(userId);
    }

    @Override
    public boolean updatePointByUserId(Long userId, Integer point) {
        return userMapper.updatePointByUserId(userId, point) > 0;
    }
}