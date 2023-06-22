package com.huiyu.service.core.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.aspect.annotation.MethodMonitor;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.SDCmdCountIntegral;
import com.huiyu.service.core.sd.SDCmdValidator;
import com.huiyu.service.core.sd.generate.AbstractImageGenerate;
import com.huiyu.service.core.service.auth.UserService;
import com.huiyu.service.core.utils.NewPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Stable Diffusion功能请求处理
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sd")
public class SDController {

    @Resource
    private List<AbstractImageGenerate> imageGenerates;

    @Resource
    private UserService userService;

    /**
     * 文生图
     *
     * @param cmd Txt2ImgCmd
     * @return 校验成功返回等待消息/校验失败返回错误消息
     */
    @MethodMonitor
    @PostMapping("/txt2img")
    public R<?> txt2img(@Valid @RequestBody Txt2ImgCmd cmd) {
        // 1. 参数校验(数值范围)
        NewPair<Boolean, String> validate = SDCmdValidator.validate(cmd);
        if (!validate.getKey()) {
            return R.error(validate.getValue());
        }

        // 2. 校验用户积分
//        Long userId = JwtUtils.getId();
        Long userId = 1L;
        int calcIntegral = SDCmdCountIntegral.calcIntegralConsume(cmd);
        int integral = userService.getIntegralById(userId);
        if (integral < calcIntegral) {
            return R.error("积分不足");
        }
        cmd.setIntegral(calcIntegral);
        cmd.setUserId(1L);


        // 3. 描述词违禁词检测



        // 4. 检验用户图片库存是否满(库存是否需要根据用户级别增加)



        // 5. 提交任务队列
        imageGenerates.stream()
                .filter(imageGenerate -> imageGenerate.isSupport(cmd))
                .forEach(imageGenerate -> imageGenerate.generate(cmd));

        // 6. 处理用户界面

        return R.ok();
    }

}
