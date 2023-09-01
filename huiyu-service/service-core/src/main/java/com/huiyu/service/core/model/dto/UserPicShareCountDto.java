package com.huiyu.service.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-09-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPicShareCountDto {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 投稿数量
     */
    private int picShareCount;
}
