package com.huiyu.service.core.service.business;

import com.huiyu.service.api.entity.User;

/**
 * @author Naccl
 * @date 2023-08-11
 */
public interface UserBusiness {
    /**
     * 修改用户数据
     *
     * @param user 实例对象
     * @return true/false
     */
    boolean updateUser(User user);
}
