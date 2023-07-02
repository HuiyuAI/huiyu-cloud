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
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Long id);

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
     * 通过userId查询单条数据
     *
     * @param userId userId
     * @return 单条数据
     */
    User queryByUserId(Long userId);

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
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 通过主键查询积分
     *
     * @param id 主键
     * @return 积分数
     */
    int getIntegralById(Long id);

    boolean updateIntegralById(Long id, Integer integral);
}
