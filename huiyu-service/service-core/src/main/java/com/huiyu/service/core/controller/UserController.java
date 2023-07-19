package com.huiyu.service.core.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.api.entity.User;
import com.huiyu.service.core.aspect.annotation.RequestLimiter;
import com.huiyu.service.core.aspect.annotation.RequestLogger;
import com.huiyu.service.core.convert.UserConvert;
import com.huiyu.service.core.model.vo.UserVo;
import com.huiyu.service.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Naccl
 * @date 2023-07-20
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /**
     * 查询当前登录用户信息
     */
    @RequestLogger
    @RequestLimiter(seconds = 60, maxCount = 120)
    @GetMapping("/getMyUserInfo")
    public R<UserVo> getMyUserInfo() {
        Long userId = JwtUtils.getUserId();
        User user = userService.queryByUserId(userId);
        UserVo userVo = UserConvert.INSTANCE.toVo(user);
        return R.ok(userVo);
    }
}
