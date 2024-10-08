package com.huiyu.service.core.model.vo;

import com.huiyu.service.core.enums.PicShareStatusEnum;
import com.huiyu.service.core.enums.PicStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-06-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicVo {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 状态
     */
    private PicStatusEnum status;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 模型id
     */
    private Long modelId;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 质量
     */
    private String quality;
    /**
     * 比例
     */
    private String ratio;
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
    private Long seed;
    /**
     * 提示词引导系数
     */
    private BigDecimal cfg;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 图片投稿状态
     */
    private PicShareStatusEnum shareStatus;
}
