package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@EqualsAndHashCode(callSuper = true)
public class Txt2ImgCmd extends Cmd {
    /**
     * 模型id
     */
    @NotNull(message = "模型id不能为空")
    private Long modelId;
    /**
     * 正向描述词
     */
    @NotBlank(message = "描述词不能为空")
    private String prompt;
    /**
     * 反向描述词
     */
    private String negativePrompt;
    /**
     * 采样步数 10-30 现在固定为20
     */
//    @Min(value = 10, message = "参数错误")
//    @Max(value = 30, message = "参数错误")
    private Integer steps;
    /**
     * 图片尺寸
     * 1: 头像     1:1   原始分辨率: 512x512   高分辨率修复至: 1024x1024
     * 2: 社交媒体  3:4   原始分辨率: 576x768   高分辨率修复至: 1152x1536
     * 3: 文章配图  4:3   原始分辨率: 768x576   高分辨率修复至: 1536x1152
     * 4: 手机壁纸  9:16  原始分辨率: 540x960   高分辨率修复至: 1080x1920
     * 5: 电脑壁纸  16:9  原始分辨率: 960x540   高分辨率修复至: 1920x1080
     */
    @Min(value = 1, message = "参数错误")
    @Max(value = 5, message = "参数错误")
    @NotNull(message = "参数错误")
    private Integer size;
    /**
     * 生成数量 1、2、3、4
     */
    @Min(value = 1, message = "参数错误")
    @Max(value = 4, message = "参数错误")
    @NotNull(message = "参数错误")
    private Integer count;
    /**
     * 图片质量
     * 1: 高清      原始分辨率1080P
     * 2: 超清      高分辨率修复至1080P
     * 3: 超高清4K  高分辨率修复至1080P，高清化至4K
     * // 4: 超清修复  文生图不提供，画夹中体验，需要一张1080P或4K图，从1080P图细节修复放大至4K，再高清化1倍至4K
     */
    @Min(value = 1, message = "参数错误")
    @Max(value = 3, message = "参数错误")
    @NotNull(message = "参数错误")
    private Integer quality;
    /**
     * 描述词相关度 3-15 步进0.5
     */
    @Min(value = 3, message = "参数错误")
    @Max(value = 15, message = "参数错误")
    private BigDecimal cfg;
    /**
     * 种子
     */
    private Long seed;

}
