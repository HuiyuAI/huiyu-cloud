package com.huiyu.service.core.model.vo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 模型表(Model)视图类
 *
 * @author Naccl
 * @date 2023-06-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelVo {

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
     * 排序
     */
    private Integer priority;
}
