package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-08-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicShareVo {
    /**
     * 图片uuid
     */
    private String uuid;
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
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
    /**
     * 采样步数
     */
    private Integer steps;
    /**
     * 提示词引导系数
     */
    private BigDecimal cfg;
    /**
     * 投稿人昵称
     */
    private String userNickname;
    /**
     * 投稿人头像
     */
    private String userAvatar;
    /**
     * 作品标题
     */
    private String title;
    /**
     * 点击量
     */
    private Integer hits;
    /**
     * 点赞量
     */
    private Integer likeCount;
    /**
     * 画同款次数
     */
    private Integer drawCount;
    /**
     * 审核通过时间
     */
    private LocalDateTime auditTime;
}
