package com.huiyu.service.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Naccl
 * @date 2023-08-12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiraiLoginInfoVo {
    private Long qq;
    private Long groupId;
}
