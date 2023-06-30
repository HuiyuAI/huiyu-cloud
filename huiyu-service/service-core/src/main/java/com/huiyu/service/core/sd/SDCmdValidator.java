package com.huiyu.service.core.sd;

import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.service.ModelService;
import com.huiyu.service.core.utils.NewPair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Naccl
 * @date 2023-06-11
 */
@Component
public class SDCmdValidator {

    private static ModelService modelService;

    @Autowired
    public void setModelService(ModelService modelService) {
        SDCmdValidator.modelService = modelService;
    }

    public static NewPair<Boolean, String> validate(Txt2ImgCmd cmd) {
        Model model = modelService.getById(cmd.getModelId());
        if (model == null) {
            return NewPair.of(false, "模型不存在");
        }

        if (cmd.getPrompt().length() > 500) {
            return NewPair.of(false, "描述词过长");
        }

        if (StringUtils.isNotBlank(cmd.getNegativePrompt()) && cmd.getNegativePrompt().length() > 500) {
            return NewPair.of(false, "负向描述词过长");
        }

        if (cmd.getSteps() == null) {
            cmd.setSteps(20);
        }


        if (cmd.getCfg() == null) {
            cmd.setCfg(BigDecimal.valueOf(9));
        } else if (cmd.getCfg().remainder(new BigDecimal("0.5")).compareTo(BigDecimal.ZERO) != 0) {
            // cfg必须是0.5的倍数
            return NewPair.of(false, "参数错误");
        }

        if (cmd.getSeed() == null) {
            cmd.setSeed(-1L);
        }

        return NewPair.of(true, null);
    }

    public static NewPair<Boolean, String> validate(Img2ImgCmd cmd) {
        return NewPair.of(true, null);
    }
}
