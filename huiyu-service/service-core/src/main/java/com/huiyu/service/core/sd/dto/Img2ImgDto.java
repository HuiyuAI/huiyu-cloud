package com.huiyu.service.core.sd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 与SD API交互的img2img数据传输对象
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Img2ImgDto extends Dto {
    /**
     * 模型
     */
    @JsonProperty("sd_model_checkpoint")
    private String sdModelCheckpoint;
}
