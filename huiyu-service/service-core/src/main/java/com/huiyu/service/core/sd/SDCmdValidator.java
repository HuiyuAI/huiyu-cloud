package com.huiyu.service.core.sd;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.lang.Pair;
import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.service.ModelService;
import com.huiyu.service.core.service.PicService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Naccl
 * @date 2023-06-11
 */
@Slf4j
@Component
public class SDCmdValidator {

    private static ModelService modelService;

    private static PicService picService;

    private static WxMaService wxMaService;

    @Autowired
    public void setModelService(ModelService modelService) {
        SDCmdValidator.modelService = modelService;
    }

    @Autowired
    public void setPicService(PicService picService) {
        SDCmdValidator.picService = picService;
    }

    @Autowired
    public void setWxMaService(WxMaService wxMaService) {
        SDCmdValidator.wxMaService = wxMaService;
    }

    public static Pair<Boolean, String> validate(Txt2ImgCmd cmd) {
        Model model = modelService.getById(cmd.getModelId(), true);
        if (model == null) {
            return Pair.of(false, "模型不存在");
        }

        if (cmd.getPrompt().length() > 500) {
            return Pair.of(false, "描述词过长");
        }

        if (StringUtils.isNotBlank(cmd.getNegativePrompt()) && cmd.getNegativePrompt().length() > 500) {
            return Pair.of(false, "负向描述词过长");
        }

        if (cmd.getSteps() == null) {
            cmd.setSteps(20);
        }


        if (cmd.getCfg() == null) {
            cmd.setCfg(BigDecimal.valueOf(9));
        } else if (cmd.getCfg().remainder(new BigDecimal("0.5")).compareTo(BigDecimal.ZERO) != 0) {
            // cfg必须是0.5的倍数
            return Pair.of(false, "参数错误");
        }

        if (cmd.getSeed() == null) {
            cmd.setSeed(-1L);
        }

        // 描述词审核
        try {
            String auditMsg = cmd.getPrompt() + "。" + cmd.getNegativePrompt();
            boolean res = wxMaService.getSecCheckService().checkMessage(auditMsg);
            log.info("调用微信文本审核接口, auditMsg: {}, res: {}", auditMsg, res);
        } catch (WxErrorException e) {
            log.error("调用微信文本审核接口, auditMsg: {}, 错误信息: {}", e.getMessage());
            return Pair.of(false, "描述词包含违规内容，多次违规可能导致封号处罚！");
        }

        RequestContext.MODEL_CONTEXT.set(model);

        return Pair.of(true, null);
    }

    public static Pair<Boolean, String> validate(Img2ImgCmd cmd) {
        return Pair.of(true, null);
    }

    public static Pair<Boolean, String> validate(RestoreFaceCmd cmd) {
        Long userId = JwtUtils.getUserId();
        Pic originPic = picService.getByUuidAndUserIdAndStatus(cmd.getImageUuid(), userId, PicStatusEnum.GENERATED);
        if (originPic == null) {
            return Pair.of(false, "图片不存在");
        }

        Model model = modelService.getById(originPic.getModelId(), true);
        if (model == null) {
            return Pair.of(false, "该图片不可修复");
        }

        RequestContext.PARENT_PIC_CONTEXT.set(originPic);
        RequestContext.MODEL_CONTEXT.set(model);

        return Pair.of(true, null);
    }
}
