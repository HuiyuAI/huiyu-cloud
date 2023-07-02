package com.huiyu.service.core.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyu.service.api.entity.User;

/**
 * (User)表服务接口
 *
 * @author Naccl
 * @date 2022-03-08
 */
public interface UserService extends IService<User> {
    /**
     * 通过userId查询单条数据
     *
     * @param userId userId
     * @return 单条数据
     */
    User queryByUserId(Long userId);

    /**
     * 通过openid查询单条数据
     *
     * @param openid openid
     * @return 实例对象
     */
    User queryByOpenid(String openid);

    /**
     * 通过username查询单条数据
     *
     * @param username username
     * @return 实例对象
     */
    User queryByUsername(String username);

    /**
     * 统计总行数
     *
     * @param user 查询条件
     * @return 总行数
     */
    long count(User user);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    boolean update(User user);

    /**
     * 通过userId查询积分
     *
     * @param userId 用户id
     * @return 积分数
     */
    int getIntegralByUserId(Long userId);

    /**
     * 根据userId增加积分
     *
     * @param userId   用户id
     * @param integral 增加的积分
     * @return 是否成功
     */
    boolean updateIntegralByUserId(Long userId, Integer integral);
}
