package com.huiyu.service.core.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(User)查询对象实体类
 *
 * @author Naccl
 * @date 2023-07-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色
     */
    private String role;
}
