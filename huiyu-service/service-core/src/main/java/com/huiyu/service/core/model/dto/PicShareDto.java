package com.huiyu.service.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author Naccl
 * @date 2023-08-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicShareDto {
    /**
     * 图片uuid
     */
    @NotEmpty(message = "异常错误")
    private String uuid;
    /**
     * 作品标题
     */
    private String title;
}
