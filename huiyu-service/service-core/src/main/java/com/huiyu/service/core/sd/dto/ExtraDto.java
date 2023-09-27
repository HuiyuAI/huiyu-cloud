package com.huiyu.service.core.sd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 与SD API交互的高清化extra数据传输对象
 *
 * @author Naccl
 * @date 2023-06-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtraDto extends Dto {
    /**
     * 参考图url地址uuid
     */
    @JsonProperty("image_uuid")
    private String imageUuid;
    /**
     * 放大倍数 1-4
     */
    @JsonProperty("upscaling_resize")
    private Integer upscalingResize;
}
