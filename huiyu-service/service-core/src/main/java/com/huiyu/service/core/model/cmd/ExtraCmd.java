package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "异常错误")
    private String imageUuid;
}
