package com.huiyu.service.core.sd;

import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
import com.huiyu.service.core.sd.constant.ImageSizeEnum;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Naccl
 * @date 2023-06-11
 */
@Component
public class SDCmd2DtoConverter {

    private static ModelService modelService;

    @Autowired
    public void setModelService(ModelService modelService) {
        SDCmd2DtoConverter.modelService = modelService;
    }

    public static Txt2ImgDto convert(Txt2ImgCmd cmd) {
        Model model = modelService.getById(cmd.getModelId(), true);

        String modelCode = model.getCode();
        String vae = model.getVae();
        String sampler = model.getSampler();
        String lora = model.getLora();
        String defaultPrompt = model.getDefaultPrompt();
        String defaultNegativePrompt = model.getDefaultNegativePrompt();

        // TODO 调用API翻译中文描述词（或根据词库映射）
        String prompt = cmd.getPrompt();
        String negativePrompt = cmd.getNegativePrompt();

        // 过滤prompt中含有<>的内容，防止使用未定义的LoRA
        prompt = prompt.replaceAll("<.*?>", "");
        negativePrompt = negativePrompt.replaceAll("<.*?>", "");

        Integer quality = cmd.getQuality();
        ImageQualityEnum imageQualityEnum = ImageQualityEnum.getEnumByCode(quality);
        String hrUpscaler = model.getHrUpscaler();

        Integer size = cmd.getSize();
        ImageSizeEnum imageSizeEnum = ImageSizeEnum.getEnumByCode(size);
        Integer width = imageQualityEnum.getOriginalScale().multiply(BigDecimal.valueOf(imageSizeEnum.getWidth())).intValue();
        Integer height = imageQualityEnum.getOriginalScale().multiply(BigDecimal.valueOf(imageSizeEnum.getHeight())).intValue();

        return Txt2ImgDto.builder()
                .sdModelCheckpoint(modelCode)
                .sdVae(vae)
                .prompt(prompt)
                .negativePrompt(negativePrompt)
                .defaultPrompt(defaultPrompt)
                .defaultNegativePrompt(defaultNegativePrompt)
                .lora(lora)
                .samplerName(sampler)
                .steps(cmd.getSteps())
                .enableHr(imageQualityEnum.getEnableHr())
                .hrUpscaler(hrUpscaler)
                .denoisingStrength(imageQualityEnum.getDenoisingStrength())
                .hrScale(imageQualityEnum.getHrScale())
                .width(width)
                .height(height)
                .batchSize(1)
                .nIter(1)
                .cfgScale(cmd.getCfg())
                .seed(cmd.getSeed())
                .enableExtra(imageQualityEnum.getEnableExtra())
                .upscalingResize(imageQualityEnum.getUpscalingResize())
                .build();
    }

    public static Img2ImgDto convert(Img2ImgCmd cmd) {
        return null;
    }
}
