package com.huiyu.service.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.huiyu.service.api.entity.User;
import com.huiyu.common.core.result.R;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户信息 FeignClient
 *
 * @author Naccl
 * @date 2022-03-08
 */
@FeignClient(value = "huiyu-service", contextId = "userFeignClient")
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
     * 通过userId查询单条数据
     *
     * @param userId userId
     * @return 单条数据
     */
    @GetMapping("/admin/user/userId/{userId}")
    R<User> queryByUserId(@PathVariable("userId") Long userId);

    /**
     * 新增数据
     *
     * @param user      实体
     * @param inviterId 邀请人id
     * @return 新增结果
     */
    @PostMapping("/admin/user/addUser")
    R<User> add(@RequestBody User user, @RequestParam("inviterId") Long inviterId);

}
