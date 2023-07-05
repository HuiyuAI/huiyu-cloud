package com.huiyu.service.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huiyu.service.core.constant.PicStatusEnum;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
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
@TableName("pic")
public class Pic implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;
    /**
     * uuid
     */
    @TableField("uuid")
    private String uuid;
    /**
     * 请求uuid
     */
    @TableField("request_uuid")
    private String requestUuid;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 任务id
     */
    @TableField("task_id")
    private Long taskId;
    /**
     * 模型id
     */
    @TableField("model_id")
    private Long modelId;
    /**
     * 源图id
     */
    @TableField("parent_pic_id")
    private Long parentPicId;
    /**
     * 参考图id
     */
    @TableField("reference_pic_id")
    private Long referencePicId;
    /**
     * 状态 0生成中 1已生成 2废弃
     */
    @TableField("status")
    private PicStatusEnum status;
    /**
     * 图片地址
     */
    @TableField("path")
    private String path;
    /**
     * 正向描述词
     */
    @TableField("prompt")
    private String prompt;
    /**
     * 反向描述词
     */
    @TableField("negative_prompt")
    private String negativePrompt;
    /**
     * 质量
     */
    @TableField("quality")
    private ImageQualityEnum quality;
    /**
     * 比例
     */
    @TableField("ratio")
    private String ratio;
    /**
     * 宽
     */
    @TableField("width")
    private Integer width;
    /**
     * 高
     */
    @TableField("height")
    private Integer height;
    /**
     * 种子
     */
    @TableField("seed")
    private Long seed;
    /**
     * 子种子
     */
    @TableField("subseed")
    private Long subseed;
    /**
     * 模型编码
     */
    @TableField("model_code")
    private String modelCode;
    /**
     * vae
     */
    @TableField("vae")
    private String vae;
    /**
     * 采样器
     */
    @TableField("sampler_name")
    private String samplerName;
    /**
     * 采样步数 10-30
     */
    @TableField("steps")
    private Integer steps;
    /**
     * 提示词引导系数 1-30 步进0.5
     */
    @TableField("cfg")
    private BigDecimal cfg;
    /**
     * 启用高分辨率修复
     */
    @TableField("enable_hr")
    private Boolean enableHr;
    /**
     * 高清化算法
     */
    @TableField("hr_upscaler")
    private String hrUpscaler;
    /**
     * 重绘强度 0.00-1.00之间两位小数
     */
    @TableField("denoising_strength")
    private BigDecimal denoisingStrength;
    /**
     * 放大倍数 1-4之间两位小数 步进0.05
     */
    @TableField("hr_scale")
    private BigDecimal hrScale;
    /**
     * 是否启用工序三高清化extra
     */
    @TableField("enable_extra")
    private Boolean enableExtra;
    /**
     * 高清化extra放大倍数 1-4
     */
    @TableField("upscaling_resize")
    private Integer upscalingResize;
    /**
     * controlnet参数
     */
    @TableField("alwayson_scripts")
    private String alwaysonScripts;
    /**
     * 通用图片描述文本
     */
    @TableField("infotexts")
    private String infotexts;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    /**
     * 是否删除1是0否
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;
}
