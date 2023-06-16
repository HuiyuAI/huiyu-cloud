package com.huiyu.service.core.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmdValidator;
import com.huiyu.service.core.sd.generate.AbstractImageGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private List<AbstractImageGenerate> imageGenerates;

    /**
     * 文生图
     *
     * @param cmd Txt2ImgCmd
     * @return 校验成功返回等待消息/校验失败返回错误消息
     */
    @PostMapping("/txt2img")
    public R<?> txt2img(@RequestBody Txt2ImgCmd cmd) {
        // 1. 校验用户积分
        Long userId = JwtUtils.getId();
        cmd.setUserId(userId);
        // 2. 参数校验(数值范围)，描述词违禁词检测
        boolean validate = SDCmdValidator.validate(cmd);
        if (!validate) {
            return R.error("参数错误");
        }

        // 3. 提交任务队列

        imageGenerates.stream()
                .filter(imageGenerate -> imageGenerate.isSupport(cmd))
                .forEach(imageGenerate -> imageGenerate.generate(cmd));
        // 4. 处理用户界面

        return R.ok();
    }
}
