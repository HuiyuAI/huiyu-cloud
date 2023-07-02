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
     * 通过userId查询单条数据
     *
     * @param userId userId
     * @return 实例对象
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
     * 通过主键查询积分
     *
     * @param userId 用户id
     * @return 积分
     */
    int getIntegralByUserId(Long userId);

    /**
     * 根据userId增加积分
     *
     * @param userId   用户id
     * @param integral 增加积分
     * @return 影响行数
     */
    int updateIntegralByUserId(@Param("userId") Long userId, @Param("integral") Integer integral);
}
