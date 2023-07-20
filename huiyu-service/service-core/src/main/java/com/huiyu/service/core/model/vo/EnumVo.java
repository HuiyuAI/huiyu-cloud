package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-07-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnumVo {
    /**
     * 枚举名称
     */
    private String key;
    /**
     * 枚举描述
     */
    private String desc;
}
