package com.huiyu.service.core.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.IdUtil;
import com.huiyu.common.core.result.R;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.aspect.annotation.MethodMonitor;
import com.huiyu.service.core.aspect.annotation.RequestLimiter;
import com.huiyu.service.core.aspect.annotation.RequestLogger;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.model.vo.SDResponseVo;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.model.vo.SpellbookVo;
import com.huiyu.service.core.sd.SDPointCalculator;
import com.huiyu.service.core.sd.SDCmdValidator;
import com.huiyu.service.core.sd.generate.AbstractImageGenerate;
import com.huiyu.service.core.service.SpellbookService;
import com.huiyu.service.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Resource
    private SpellbookService spellbookService;

    @Resource
    private WxMaService wxMaService;

    /**
     * 文生图
     *
     * @param cmd Txt2ImgCmd
     * @return 校验成功返回等待消息/校验失败返回错误消息
     */
    @MethodMonitor
    @RequestLogger
    @RequestLimiter(seconds = 10, maxCount = 6)
    @PostMapping("/txt2img")
    public R<String> txt2img(@Valid @RequestBody Txt2ImgCmd cmd) {
        // 1. 参数校验(数值范围)
        Pair<Boolean, String> validate = SDCmdValidator.validate(cmd);
        if (!validate.getKey()) {
            return R.error(validate.getValue());
        }

        // 2. 校验用户积分
        Long userId = JwtUtils.getUserId();
        int calcPoint = SDPointCalculator.calcPointConsume(cmd);
        int point = userService.getPointByUserId(userId);
        if (point < calcPoint) {
            return R.error("积分不足");
        }
        cmd.setPoint(calcPoint);
        cmd.setUserId(userId);


        // 3. 描述词违禁词检测
        // 描述词审核
        try {
            String auditMsg = cmd.getPrompt() + "。" + cmd.getNegativePrompt();
            boolean res = wxMaService.getSecCheckService().checkMessage(auditMsg);
            log.info("调用微信文本审核接口, auditMsg: {}, res: {}", auditMsg, res);
        } catch (WxErrorException e) {
            log.error("调用微信文本审核接口, auditMsg: {}, 错误信息: {}", e.getMessage());
            return R.error("描述词包含违规内容，多次违规可能导致封号处罚！");
        }


        // 4. 检验用户图片库存是否满(库存是否需要根据用户级别增加)


        String requestUuid = IdUtil.fastUUID();
        RequestContext.REQUEST_UUID_CONTEXT.set(requestUuid);
        RequestContext.CMD_CONTEXT.set(cmd);

        SDResponseVo sdResponseVo = new SDResponseVo();

        // 5. 提交任务队列
        imageGenerates.stream()
                .filter(imageGenerate -> imageGenerate.isSupport(cmd))
                .forEach(imageGenerate -> imageGenerate.generate(cmd, sdResponseVo));

        // 6. 处理用户界面

        return R.ok(sdResponseVo.getUuid());
    }


    /**
     * 脸部修复
     */
    @MethodMonitor
    @RequestLogger
    @RequestLimiter(seconds = 10, maxCount = 6)
    @PostMapping("/restoreFace")
    public R<String> restoreFace(@Valid @RequestBody RestoreFaceCmd cmd) {
        // 1. 参数校验
        Pair<Boolean, String> validate = SDCmdValidator.validate(cmd);
        if (!validate.getKey()) {
            return R.error(validate.getValue());
        }

        // 2. 校验用户积分
        Long userId = JwtUtils.getUserId();
        int calcPoint = SDPointCalculator.calcPointConsume(cmd);
        int point = userService.getPointByUserId(userId);
        if (point < calcPoint) {
            return R.error("积分不足");
        }
        cmd.setPoint(calcPoint);
        cmd.setUserId(userId);


        // 3. 检验用户图片库存是否满(库存是否需要根据用户级别增加)


        String requestUuid = IdUtil.fastUUID();
        RequestContext.REQUEST_UUID_CONTEXT.set(requestUuid);
        RequestContext.CMD_CONTEXT.set(cmd);

        SDResponseVo sdResponseVo = new SDResponseVo();

        // 4. 提交任务队列
        imageGenerates.stream()
                .filter(imageGenerate -> imageGenerate.isSupport(cmd))
                .forEach(imageGenerate -> imageGenerate.generate(cmd, sdResponseVo));

        // 5. 处理用户界面

        return R.ok(requestUuid);
    }

    @RequestLogger
    @RequestLimiter(seconds = 10, maxCount = 6)
    @GetMapping("/spellbook")
    public R<List<SpellbookVo>> spellbook() {
        List<SpellbookVo> spellbookVoList = spellbookService.listVo();
        return R.ok(spellbookVoList);
    }
}
