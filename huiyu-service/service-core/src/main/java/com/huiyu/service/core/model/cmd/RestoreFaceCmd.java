package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 脸部修复指令对象
 *
 * @author Naccl
 * @date 2023-07-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RestoreFaceCmd extends Cmd {
    /**
     * 原图uuid
     */
    private String imageUuid;
}
