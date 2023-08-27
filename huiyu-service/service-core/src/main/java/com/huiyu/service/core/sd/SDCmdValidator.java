package com.huiyu.service.core.sd;

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

    @Autowired
    public void setModelService(ModelService modelService) {
        SDCmdValidator.modelService = modelService;
    }

    @Autowired
    public void setPicService(PicService picService) {
        SDCmdValidator.picService = picService;
    }

    public static Pair<Boolean, String> validate(Txt2ImgCmd cmd) {
        Model model = modelService.getById(cmd.getModelId(), true);
        if (model == null) {
            return Pair.of(false, "模型不存在");
        }

        if (cmd.getPrompt().length() > 1000) {
            return Pair.of(false, "描述词过长");
        }

        if (StringUtils.isNotBlank(cmd.getNegativePrompt()) && cmd.getNegativePrompt().length() > 1000) {
            return Pair.of(false, "负向描述词过长");
        }

//        if (cmd.getSteps() == null) {
//            cmd.setSteps(20);
//        }
        // 固定20步
        cmd.setSteps(20);

        if (cmd.getCfg() == null) {
            cmd.setCfg(BigDecimal.valueOf(9));
        } else if (cmd.getCfg().remainder(new BigDecimal("0.5")).compareTo(BigDecimal.ZERO) != 0) {
            // cfg必须是0.5的倍数
            return Pair.of(false, "参数错误");
        }

        if (cmd.getSeed() == null) {
            cmd.setSeed(-1L);
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
