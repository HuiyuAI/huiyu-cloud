package com.huiyu.service.core.sd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 与SD API交互的txt2img数据传输对象
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Txt2ImgDto extends Dto {
    /**
     * 模型
     */
    @JsonProperty("sd_model_checkpoint")
    private String sdModelCheckpoint;
    /**
     * vae
     */
    @JsonProperty("sd_vae")
    private String sdVae;
    /**
     * 正向描述词
     */
    @JsonProperty("prompt")
    private String prompt;
    /**
     * 反向描述词
     */
    @JsonProperty("negative_prompt")
    private String negativePrompt;
    /**
     * 采样器
     */
    @JsonProperty("sampler_name")
    private String samplerName;
    /**
     * 采样步数 10-30
     */
    @JsonProperty("steps")
    private Integer steps;
    /**
     * 高分辨率修复 True/False
     */
    @JsonProperty("enable_hr")
    private Boolean enableHr;
    /**
     * 高清化算法
     */
    @JsonProperty("hr_upscaler")
    private String hrUpscaler;
    /**
     * 重绘强度 0.00-1.00之间两位小数
     */
    @JsonProperty("denoising_strength")
    private BigDecimal denoisingStrength;
    /**
     * 放大倍数 1-4之间两位小数 步进0.05
     */
    @JsonProperty("hr_scale")
    private BigDecimal hrScale;
    /**
     * 宽
     */
    @JsonProperty("width")
    private Integer width;
    /**
     * 高
     */
    @JsonProperty("height")
    private Integer height;
    /**
     * 每次生成的图片数量
     */
    @JsonProperty("batch_size")
    private Integer batchSize;
    /**
     * 生成次数
     */
    @JsonProperty("n_iter")
    private Integer nIter;
    /**
     * 提示词引导系数 3-15 步进0.5
     */
    @JsonProperty("cfg_scale")
    private BigDecimal cfgScale;
    /**
     * 种子
     */
    @JsonProperty("seed")
    private Integer seed;
}
