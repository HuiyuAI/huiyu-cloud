package com.huiyu.service.core.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.api.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (User)表数据库访问层
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

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
     * @return 实例对象
     */
    User queryByUserId(Long userId);

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
