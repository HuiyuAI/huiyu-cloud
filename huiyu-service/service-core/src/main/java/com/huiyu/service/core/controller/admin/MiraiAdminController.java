package com.huiyu.service.core.controller.admin;

import com.huiyu.common.core.result.R;
import com.huiyu.service.core.model.vo.MiraiLoginInfoVo;
import com.huiyu.service.core.model.vo.MiraiStatusVo;
import com.huiyu.service.core.utils.mirai.MiraiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * QQ机器人后台管理
 *
 * @author Naccl
 * @date 2023-08-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/mirai")
public class MiraiAdminController {
    /**
     * 登录QQ机器人
     */
    @PostMapping("/login")
    public R<?> login(Long qq, Long groupId) {
        new Thread(() -> MiraiUtils.login(qq, groupId)).start();
        return R.ok();
    }

    /**
     * 关闭QQ机器人
     */
    @PostMapping("/close")
    public R<?> close() {
        MiraiUtils.close();
        return R.ok();
    }

    /**
     * 获取登录二维码
     */
    @GetMapping("/getStatus")
    public R<MiraiStatusVo> getStatus() throws IOException {
        MiraiStatusVo miraiStatusVo = MiraiUtils.getStatus();
        return R.ok(miraiStatusVo);
    }

    /**
     * 获取已登录的账号信息
     */
    @GetMapping("/getLoginInfo")
    public R<MiraiLoginInfoVo> getLoginInfo() {
        return R.ok(new MiraiLoginInfoVo(MiraiUtils.getQq(), MiraiUtils.getGroupId()));
    }
}
