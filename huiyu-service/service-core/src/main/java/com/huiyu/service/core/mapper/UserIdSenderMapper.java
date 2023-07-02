package com.huiyu.service.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huiyu.service.core.entity.UserIdSender;
import org.apache.ibatis.annotations.Mapper;

/**
 * (UserIdSender)Mapper接口
 *
 * @author Naccl
 * @date 2023-07-03
 */
@Mapper
public interface UserIdSenderMapper extends BaseMapper<UserIdSender> {

    /**
     * 查询第一条记录
     */
    UserIdSender getFirst();

    /**
     * 根据id删除
     */
    int deleteById(Long id);
}
