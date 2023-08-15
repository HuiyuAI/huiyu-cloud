package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wAnG
 * @Date 2023-08-16  01:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SDResponseVo {
    /**
     * 图片uuid
     */
    private String uuid;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
}
