package com.huiyu.service.core.sd;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.constant.HuiyuConstant;
import com.huiyu.service.core.constant.TaskTypeEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.sd.dto.ExtraDto;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.sd.dto.UpscaleDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-06-11
 */
@Component
public class SDTaskConverter {

    public static Pic convert(Task task) {
        TaskTypeEnum type = task.getType();
        switch (type) {
            case TXT2IMG:
                return convertTxt2Img(task);
            case IMG2IMG:
                return convertImg2Img(task);
            case UPSCALE:
                return convertUpscale(task);
            case EXTRA:
                return convertExtra(task);
            default:
                return null;
        }
    }

    private static Pic convertTxt2Img(Task task) {
        Txt2ImgDto dto = JacksonUtils.toBean(task.getBody(), Txt2ImgDto.class);
        LocalDateTime now = LocalDateTime.now();

        int width = dto.getWidth();
        int height = dto.getHeight();
        if (dto.getEnableHr()) {
            width = dto.getHrScale().multiply(BigDecimal.valueOf(width)).intValue();
            height = dto.getHrScale().multiply(BigDecimal.valueOf(height)).intValue();
        }

        return Pic.builder()
                .uuid(dto.getResImageUuid())
                .userId(task.getUserId())
                .path(HuiyuConstant.cdnUrlGen + dto.getResImageUuid() + HuiyuConstant.imageSuffix)
                .prompt(dto.getPrompt())
                .negativePrompt(dto.getNegativePrompt())
                .width(width)
                .height(height)
                .modelCode(dto.getSdModelCheckpoint())
                .vae(dto.getSdVae())
                .samplerName(dto.getSamplerName())
                .steps(dto.getSteps())
                .cfg(dto.getCfgScale())
                .enableHr(dto.getEnableHr())
                .hrUpscaler(dto.getHrUpscaler())
                .denoisingStrength(dto.getDenoisingStrength())
                .hrScale(dto.getHrScale())
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
    }

    // TODO
    private static Pic convertImg2Img(Task task) {
        Img2ImgDto dto = JacksonUtils.toBean(task.getBody(), Img2ImgDto.class);
        LocalDateTime now = LocalDateTime.now();


        return Pic.builder()
                .uuid(dto.getResImageUuid())
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
    }

    // TODO
    private static Pic convertUpscale(Task task) {
        UpscaleDto dto = JacksonUtils.toBean(task.getBody(), UpscaleDto.class);
        LocalDateTime now = LocalDateTime.now();


        return Pic.builder()
                .uuid(dto.getResImageUuid())
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
    }

    // TODO
    private static Pic convertExtra(Task task) {
        ExtraDto dto = JacksonUtils.toBean(task.getBody(), ExtraDto.class);
        LocalDateTime now = LocalDateTime.now();


        return Pic.builder()
                .uuid(dto.getResImageUuid())
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
    }

}
