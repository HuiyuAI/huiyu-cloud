package com.huiyu.service.core.model.cmd;

import com.huiyu.service.core.sd.dto.Dto;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public class Img2ImgCmd extends Cmd{
    /**
     * 模型id
     */
    private Integer modelId;

    @Override
    public Dto toDto() {
        return new Img2ImgDto();
    }
}
