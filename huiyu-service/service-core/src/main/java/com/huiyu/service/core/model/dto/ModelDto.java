package com.huiyu.service.core.model.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author Naccl
 * @date 2023-06-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {

    /**
     * 模型id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 分类
     */
    private String category;
    /**
     * 描述
     */
    private String description;
    /**
     * 封面图片url
     */
    private String coverUrl;
    /**
     * 编码
     */
    private String code;
    /**
     * 首选vae
     */
    private String vae;
    /**
     * 首选采样器
     */
    private String sampler;
    /**
     * 首选高清化算法
     */
    private String hrUpscaler;
    /**
     * 默认正向描述词
     */
    private String defaultPrompt;
    /**
     * 默认反向描述词
     */
    private String defaultNegativePrompt;
    /**
     * 搭配lora
     */
    private String lora;
    /**
     * 排序
     */
    private Integer priority;
    /**
     * 是否启用1是0否
     */
    private Integer enabled;
}
