package com.huiyu.service.core.sd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 与SD API交互的细节修复upscale数据传输对象
 *
 * @author Naccl
 * @date 2023-06-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpscaleDto extends Dto {
    /**
     * 参考图uuid
     */
    @JsonProperty("image_uuid")
    private String imageUuid;
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
     * 重绘强度 0.00-1.00之间两位小数
     */
    @JsonProperty("denoising_strength")
    private BigDecimal denoisingStrength;
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
