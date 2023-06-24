package com.huiyu.service.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-06-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicDto {
    /**
     * uuid
     */
    private String uuid;
}
