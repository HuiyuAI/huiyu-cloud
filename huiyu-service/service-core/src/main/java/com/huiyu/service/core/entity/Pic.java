package com.huiyu.service.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 图片表(Pic)实体类
 *
 * @author Naccl
 * @date 2023-06-09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pic implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 模型id
     */
    private Integer modelId;
    /**
     * 源图id
     */
    private Long parentPicId;
    /**
     * 参考图id
     */
    private Long referencePicId;
    /**
     * 状态 0生成中 1已生成
     */
    private Integer status;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 正向描述词
     */
    private String prompt;
    /**
     * 反向描述词
     */
    private String negativePrompt;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
    /**
     * 种子
     */
    private String seed;
    /**
     * 子种子
     */
    private String subseed;
    /**
     * 模型编码
     */
    private String modelCode;
    /**
     * vae
     */
    private String vae;
    /**
     * 采样器
     */
    private String samplerName;
    /**
     * 采样步数 10-30
     */
    private Integer steps;
    /**
     * 提示词引导系数 1-30 步进0.5
     */
    private BigDecimal cfg;
    /**
     * 启用高分辨率修复
     */
    private Boolean enableHr;
    /**
     * 高清化算法
     */
    private String hrUpscaler;
    /**
     * 重绘强度 0.00-1.00之间两位小数
     */
    private BigDecimal denoisingStrength;
    /**
     * 放大倍数 1-4之间两位小数 步进0.05
     */
    private BigDecimal hrScale;
    /**
     * controlnet参数
     */
    private String alwaysonScripts;
    /**
     * 通用图片描述文本
     */
    private String infotexts;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    private Integer isDelete;
}
