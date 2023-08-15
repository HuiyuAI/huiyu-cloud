package com.huiyu.service.core.model.vo;

import com.huiyu.service.core.enums.PicStatusEnum;
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
