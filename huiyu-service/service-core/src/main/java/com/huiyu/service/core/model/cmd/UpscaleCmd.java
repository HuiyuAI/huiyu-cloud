package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 细节修复upscale指令对象
 *
 * @author Naccl
 * @date 2023-06-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpscaleCmd extends Cmd {
    /**
     * 原图uuid
     */
    private String imageUuid;
    /**
     * 色彩鲜艳度(描述词相关度) 3-15 步进0.5 过低会模糊，过高会过饱和，推荐7-11
     */
    private BigDecimal cfg;
}
