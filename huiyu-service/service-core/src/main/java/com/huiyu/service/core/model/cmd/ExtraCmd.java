package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 高清化extra指令对象
 *
 * @author Naccl
 * @date 2023-06-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtraCmd extends Cmd {
    /**
     * 原图uuid
     */
    private String imageUuid;
    /**
     * 放大等级
     * 1: 4K
     * 2: 8K
     */
    private Integer level;
}
