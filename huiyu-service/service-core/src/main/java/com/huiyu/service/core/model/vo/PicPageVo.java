package com.huiyu.service.core.model.vo;

import com.huiyu.service.core.constant.PicStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-07-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicPageVo {
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
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
}
