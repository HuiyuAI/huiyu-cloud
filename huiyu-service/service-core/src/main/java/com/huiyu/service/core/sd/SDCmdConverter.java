package com.huiyu.service.core.sd;

import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.dto.Img2ImgDTO;
import com.huiyu.service.core.sd.dto.Txt2ImgDTO;

import java.math.BigDecimal;

/**
 * @author Naccl
 * @date 2023-06-11
 */
public class SDCmdConverter {
    public static Txt2ImgDTO convert(Txt2ImgCmd cmd) {
        // TODO 根据模型id查模型名称、vae、采样器
        Integer modelId = cmd.getModelId();
        String modelName = "mix-pro-v3-non-ema-fp32.safetensors [9b307cd7a8]";
        String vae = "vae-ft-mse-840000-ema-pruned.ckpt";
        String sampler = "DPM++ 2M Karras";

        // TODO 调用API翻译中文描述词（或根据词库映射）
        String prompt = cmd.getPrompt();
        String negativePrompt = cmd.getNegativePrompt();

        // TODO 根据质量等级设置高清化参数
        Integer quality = cmd.getQuality();
        boolean enableHr = true;
        String hrUpscaler = "Latent";
        BigDecimal denoisingStrength = new BigDecimal("0.6");
        BigDecimal hrScale = new BigDecimal("2");

        // TODO 根据图片尺寸设置对应宽高
        Integer size = cmd.getSize();
        Integer width = 512;
        Integer height = 512;

        // TODO 生成数量考虑拆分成多个任务
        Integer count = cmd.getCount();
        Integer batchSize = 1;


        return Txt2ImgDTO.builder()
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

    public static Img2ImgDTO convert(Img2ImgCmd cmd) {
        return null;
    }
}
