package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * txt2img指令对象
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Txt2ImgCmd {
    /**
     * 模型id
     */
    private Integer modelId;
    /**
     * 正向描述词
     */
    private String prompt;
    /**
     * 反向描述词
     */
    private String negativePrompt;
    /**
     * 采样步数 10-30
     */
    private Integer steps;
    /**
     * 图片尺寸
     * 1: 头像框1:1
     * 2: 社交媒体3:4
     * 3: 文章配图4:3
     * 4: 手机壁纸9:16
     * 5: 电脑壁纸16:9
     */
    private Integer size;
    /**
     * 生成数量 1、2、4
     */
    private Integer count;
    /**
     * 图片质量
     * 1: 高清
     * 2: 超清
     * 3: 超清修复
     * 4: 超清精绘
     */
    private Integer quality;
    /**
     * 描述词相关度 3-15 步进0.5
     */
    private BigDecimal cfg;
    /**
     * 种子
     */
    private Integer seed;
}
