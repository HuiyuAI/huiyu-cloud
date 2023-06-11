package com.huiyu.service.core.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * img2img指令对象
 *
 * @author Naccl
 * @date 2023-06-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Img2ImgCmd {
    /**
     * 模型id
     */
    private Integer modelId;
}
