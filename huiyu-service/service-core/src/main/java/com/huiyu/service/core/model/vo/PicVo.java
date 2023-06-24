package com.huiyu.service.core.model.vo;

import com.huiyu.service.core.constant.PicStatusEnum;
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
     * 模型id
     */
    private Integer modelId;
    /**
     * 状态 0生成中 1已生成
     */
    private PicStatusEnum status;
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
    private Long seed;
    /**
     * 采样步数 10-30
     */
    private Integer steps;
    /**
     * 提示词引导系数 1-30 步进0.5
     */
    private BigDecimal cfg;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
