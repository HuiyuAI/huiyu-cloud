package com.huiyu.service.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-07-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPicCountDto {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 图片数量
     */
    private int picCount;
}
