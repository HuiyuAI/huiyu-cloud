package com.huiyu.service.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
     * 图片名称
     */
    private String name;
    /**
     * 图片地址
     */
    private String path;
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
     * 参考图id
     */
    private Long referencePicId;
    /**
     * 源图id
     */
    private Long parentPicId;
    /**
     * 用户id
     */
    private Long userId;
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
