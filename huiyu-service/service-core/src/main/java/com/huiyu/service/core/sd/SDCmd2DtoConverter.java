package com.huiyu.service.core.sd;

import com.fasterxml.jackson.databind.JsonNode;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.entity.Model;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.model.cmd.ExtraCmd;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
import com.huiyu.service.core.sd.constant.ImageSizeEnum;
import com.huiyu.service.core.sd.dto.ExtraDto;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import com.huiyu.service.core.sd.dto.RestoreFaceDto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.utils.translate.TencentCloudTranslator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Naccl
 * @date 2023-06-11
 */
public class SDCmd2DtoConverter {

    public static Txt2ImgDto convert(Txt2ImgCmd cmd) {
        Model model = RequestContext.MODEL_CONTEXT.get();

        String modelCode = model.getCode();
        String vae = model.getVae();
        String sampler = model.getSampler();
        String lora = model.getLora();
        String defaultPrompt = model.getDefaultPrompt();
        String defaultNegativePrompt = model.getDefaultNegativePrompt();

        String prompt = cmd.getPrompt();
        String negativePrompt = cmd.getNegativePrompt();

        // 过滤prompt中含有<>的内容，防止使用未定义的LoRA
        prompt = prompt.replaceAll("<.*?>", "");
        negativePrompt = negativePrompt.replaceAll("<.*?>", "");

        List<String> promptList = Arrays.asList(prompt, negativePrompt);
        List<String> translateResList = TencentCloudTranslator.en2ZhTranslateBatch(promptList);
        String translatedPrompt = translateResList.get(0).substring(0, Math.min(translateResList.get(0).length(), 2000));
        String translatedNegativePrompt = translateResList.get(1).substring(0, Math.min(translateResList.get(1).length(), 2000));

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
                .prompt(translatedPrompt)
                .negativePrompt(translatedNegativePrompt)
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

    public static RestoreFaceDto convert(RestoreFaceCmd cmd) {
        Pic parentPic = RequestContext.PARENT_PIC_CONTEXT.get();
        Model model = RequestContext.MODEL_CONTEXT.get();

        String modelCode = model.getCode();
        String vae = model.getVae();
        String sampler = model.getSampler();
        String lora = model.getLora();
        String defaultPrompt = model.getDefaultPrompt();
        String defaultNegativePrompt = model.getDefaultNegativePrompt();

        ImageQualityEnum imageQualityEnum = parentPic.getQuality();
        String hrUpscaler = model.getHrUpscaler();

        ImageSizeEnum imageSizeEnum = ImageSizeEnum.getEnumByRatio(parentPic.getRatio());
        Integer width = imageQualityEnum.getOriginalScale().multiply(BigDecimal.valueOf(imageSizeEnum.getWidth())).intValue();
        Integer height = imageQualityEnum.getOriginalScale().multiply(BigDecimal.valueOf(imageSizeEnum.getHeight())).intValue();

        // 脸部修复脚本参数
        JsonNode alwaysonScripts = JacksonUtils.toBean("{\"ADetailer\": {\"args\": [{\"ad_model\": \"face_yolov8n.pt\"}]}}", JsonNode.class);

        return RestoreFaceDto.builder()
                .sdModelCheckpoint(modelCode)
                .sdVae(vae)
                .prompt(parentPic.getTranslatedPrompt())
                .negativePrompt(parentPic.getTranslatedNegativePrompt())
                .defaultPrompt(defaultPrompt)
                .defaultNegativePrompt(defaultNegativePrompt)
                .lora(lora)
                .samplerName(sampler)
                .steps(parentPic.getSteps())
                .enableHr(imageQualityEnum.getEnableHr())
                .hrUpscaler(hrUpscaler)
                .denoisingStrength(imageQualityEnum.getDenoisingStrength())
                .hrScale(imageQualityEnum.getHrScale())
                .width(width)
                .height(height)
                .batchSize(1)
                .nIter(1)
                .cfgScale(parentPic.getCfg())
                .seed(parentPic.getSeed())
                .enableExtra(imageQualityEnum.getEnableExtra())
                .upscalingResize(imageQualityEnum.getUpscalingResize())
                .alwaysonScripts(alwaysonScripts)
                .build();
    }

    public static ExtraDto convert(ExtraCmd cmd) {
        Pic parentPic = RequestContext.PARENT_PIC_CONTEXT.get();

        return ExtraDto.builder()
                .imageUuid(parentPic.getUrlUuid())
                .upscalingResize(ImageQualityEnum.UHD4K.getUpscalingResize())
                .build();
    }

}
