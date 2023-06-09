package com.huiyu.auth.security.core.userdetails;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.huiyu.auth.domain.User;
import com.huiyu.common.core.result.R;

/**
 * 用户信息 FeignClient
 *
 * @author Naccl
 * @date 2022-03-08
 */
@FeignClient(value = "clh-service-user", contextId = "userFeignClient")
public interface UserFeignClient {
    /**
     * 通过openid查询单条数据
     *
     * @param openid openid
     * @return 单条数据
     */
    @GetMapping("/admin/user/openid/{openid}")
    R<User> queryByOpenid(@PathVariable("openid") String openid);

    /**
     * 通过username查询单条数据
     *
     * @param username username
     * @return 单条数据
     */
    @GetMapping("/admin/user/username/{username}")
    R<User> queryByUsername(@PathVariable("username") String username);

    /**
     * 新增数据
     *
     * @param user 实体
     * @return 新增结果
     */
    @PostMapping("/admin/user")
    R<User> add(@RequestBody User user);

    /**
     * 统计总行数
     *
     * @param user 筛选条件
     * @return 查询结果
     */
    @GetMapping("/admin/user/count")
    R<Long> count(@SpringQueryMap User user);
}
