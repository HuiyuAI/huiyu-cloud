package com.huiyu.service.core.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Stable Diffusion功能请求处理
 *
 * @author Naccl
 * @date 2023-06-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sd")
public class SDController {

    @PostMapping("/txt2img")
    public R<?> txt2img(@RequestBody Txt2ImgCmd cmd) {
        // 1. 校验用户积分
        Long userId = JwtUtils.getId();

        // 2. 参数校验(数值范围)，描述词违禁词检测
        boolean validate = SDCmdValidator.validate(cmd);
        if (!validate) {
            return R.error("参数错误");
        }

        // 3. 提交任务队列

        // 4. 处理用户界面

        return R.ok();
    }
}
