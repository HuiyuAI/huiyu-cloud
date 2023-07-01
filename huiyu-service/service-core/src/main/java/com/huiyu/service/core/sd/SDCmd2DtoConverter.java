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
        Model model = modelService.getById(cmd.getModelId());

        String modelCode = model.getCode();
        String vae = model.getVae();
        String sampler = model.getSampler();

        // TODO 调用API翻译中文描述词（或根据词库映射）
        String prompt = cmd.getPrompt();
        String negativePrompt = cmd.getNegativePrompt();

        Integer quality = cmd.getQuality();
        ImageQualityEnum imageQualityEnum = ImageQualityEnum.getEnumByCode(quality);
        String hrUpscaler = model.getHrUpscaler();

        Integer size = cmd.getSize();
        ImageSizeEnum imageSizeEnum = ImageSizeEnum.getEnumByCode(size);
        Integer width = imageSizeEnum.getWidth();
        Integer height = imageSizeEnum.getHeight();

        if (imageQualityEnum == ImageQualityEnum.HD) {
            // 高清质量，不启用高分辨率修复，直接翻倍原始尺寸
            width *= 2;
            height *= 2;
        }

        return Txt2ImgDto.builder()
                .sdModelCheckpoint(modelCode)
                .sdVae(vae)
                .prompt(prompt)
                .negativePrompt(negativePrompt)
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
