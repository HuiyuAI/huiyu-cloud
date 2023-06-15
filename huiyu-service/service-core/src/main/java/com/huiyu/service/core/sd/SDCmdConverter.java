package com.huiyu.service.core.sd;

import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;

import java.math.BigDecimal;

/**
 * @author Naccl
 * @date 2023-06-11
 */
public class SDCmdConverter {
    public static Txt2ImgDto convert(Txt2ImgCmd cmd) {
        // TODO 根据模型id查模型名称、vae、采样器
        Integer modelId = cmd.getModelId();
        String modelName = "mix-pro-v3-non-ema-fp32.safetensors [9b307cd7a8]";
        String vae = "vae-ft-mse-840000-ema-pruned.ckpt";
        String sampler = "DPM++ 2M Karras";

        // TODO 调用API翻译中文描述词（或根据词库映射）
        String prompt = cmd.getPrompt();
        String negativePrompt = cmd.getNegativePrompt();

        // TODO 根据质量等级设置高清化参数
        boolean enableHr = true;
        // 极少情况下，不同的模型、参数可能需要"R-ESRGAN 4x+ Anime6B"
        String hrUpscaler = "Latent";
        BigDecimal denoisingStrength = new BigDecimal("0.6");
        BigDecimal hrScale = new BigDecimal("2");

        switch (cmd.getQuality()) {
            case 1:
                // 高清
                enableHr = false;
                break;
            case 2:
                // 超清
                enableHr = true;
                break;
            case 3:
                // 超清修复 第一道工序+第二道工序
                enableHr = true;
                break;
            case 4:
                // 超清精绘 三道工序
                enableHr = true;
                break;
            default:
                break;
        }

        // TODO 根据图片尺寸设置对应宽高
        Integer size = cmd.getSize();
        Integer width = 512;
        Integer height = 512;

        // TODO 生成数量考虑拆分成多个任务
        Integer count = cmd.getCount();
        Integer batchSize = 1;


        return Txt2ImgDto.builder()
                .sdModelCheckpoint(modelName)
                .sdVae(vae)
                .prompt(prompt)
                .negativePrompt(negativePrompt)
                .samplerName(sampler)
                .steps(cmd.getSteps())
                .enableHr(enableHr)
                .hrUpscaler(hrUpscaler)
                .denoisingStrength(denoisingStrength)
                .hrScale(hrScale)
                .width(width)
                .height(height)
                .batchSize(batchSize)
                .nIter(1)
                .cfgScale(cmd.getCfg())
                .seed(cmd.getSeed())
                .build();
    }

    public static Img2ImgDto convert(Img2ImgCmd cmd) {
        return null;
    }
}
