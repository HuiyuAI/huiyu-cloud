package com.huiyu.service.core.mapper.auth;

import com.huiyu.service.api.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * 查询指定行数据
     *
     * @param user 查询条件
     * @return 对象列表
     */
    List<User> queryAll(User user);

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
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 通过主键查询积分
     *
     * @param id 主键
     * @return 积分数
     */
    int getIntegralById(Long id);

    int updateIntegralById(@Param("id") Long id, @Param("integral") Integer integral);
}
