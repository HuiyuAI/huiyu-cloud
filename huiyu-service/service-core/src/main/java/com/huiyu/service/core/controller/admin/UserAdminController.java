package com.huiyu.service.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huiyu.common.core.result.R;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.model.query.UserQuery;
import com.huiyu.service.core.model.vo.UserAdminVo;
import com.huiyu.service.core.service.UserService;
import com.huiyu.service.core.service.business.UserBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final UserBusiness userBusiness;

    @GetMapping("/{pageNum}/{pageSize}")
    public R<IPage<UserAdminVo>> adminPageQuery(UserQuery query, @PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        IPage<UserAdminVo> pageInfo = userBusiness.adminPageQuery(new Page<>(pageNum, pageSize), query);
        return R.ok(pageInfo);
    }

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
     * 新增数据
     *
     * @param user      实体
     * @param inviterId 邀请人id
     * @return 新增结果
     */
    @PostMapping("/addUser")
    public R<User> add(@RequestBody User user, @RequestParam("inviterId") Long inviterId) {
        return R.ok(userBusiness.addUser(user, inviterId));
    }

    /**
     * 编辑数据
     *
     * @param user 实体
     * @return 编辑结果
     */
    @PostMapping("/updateUser")
    public <T> R<T> update(@RequestBody User user) {
        userBusiness.updateUser(user);
        return R.ok();
    }

}
