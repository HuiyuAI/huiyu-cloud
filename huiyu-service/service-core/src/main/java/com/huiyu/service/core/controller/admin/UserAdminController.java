package com.huiyu.service.core.controller.admin;

import com.huiyu.common.core.result.R;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (User)表控制层
 *
 * @author Naccl
 * @date 2022-03-08
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {
    private final UserService userService;

    /**
     * 通过userId查询单条数据
     *
     * @param userId userId
     * @return 单条数据
     */
    @GetMapping("/userId/{userId}")
    public R<User> queryByUserId(@PathVariable("userId") Long userId) {
        return R.ok(userService.queryByUserId(userId));
    }

    /**
     * 通过openid查询单条数据
     *
     * @param openid openid
     * @return 单条数据
     */
    @GetMapping("/openid/{openid}")
    public R<User> queryByOpenid(@PathVariable("openid") String openid) {
        return R.ok(userService.queryByOpenid(openid));
    }

    /**
     * 通过username查询单条数据
     *
     * @param username username
     * @return 单条数据
     */
    @GetMapping("/username/{username}")
    public R<User> queryByUsername(@PathVariable("username") String username) {
        return R.ok(userService.queryByUsername(username));
    }

    /**
     * 统计总行数
     *
     * @param user 筛选条件
     * @return 查询结果
     */
    @GetMapping("/count")
    public R<Long> count(User user) {
        return R.ok(userService.count(user));
    }

    /**
     * 新增数据
     *
     * @param user 实体
     * @return 新增结果
     */
    @PostMapping
    public R<User> add(@RequestBody User user) {
        return R.ok(userService.insert(user));
    }

    /**
     * 编辑数据
     *
     * @param user 实体
     * @return 编辑结果
     */
    @PutMapping
    public <T> R<T> edit(@RequestBody User user) {
        userService.update(user);
        return R.ok();
    }

}
