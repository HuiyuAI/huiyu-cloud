package com.huiyu.service.core.sd;

import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.config.RequestContext;
import com.huiyu.service.core.enums.TaskTypeEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.entity.Task;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;
import com.huiyu.service.core.sd.constant.ImageQualityEnum;
import com.huiyu.service.core.sd.constant.ImageSizeEnum;
import com.huiyu.service.core.sd.dto.ExtraDto;
import com.huiyu.service.core.sd.dto.Img2ImgDto;
import com.huiyu.service.core.sd.dto.RestoreFaceDto;
import com.huiyu.service.core.sd.dto.Txt2ImgDto;
import com.huiyu.service.core.sd.dto.UpscaleDto;
import com.huiyu.service.core.utils.IdUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Naccl
 * @date 2023-06-11
 */
@Component
public class SDTask2PicConverter {

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
            case RESTORE_FACE:
                return convertRestoreFace(task);
            default:
                return null;
        }
    }

    private static Pic convertTxt2Img(Task task) {
        Txt2ImgDto dto = JacksonUtils.toBean(task.getBody(), Txt2ImgDto.class);
        LocalDateTime now = task.getCreateTime();

        int width = dto.getWidth();
        int height = dto.getHeight();
        if (dto.getEnableHr()) {
            width = dto.getHrScale().multiply(BigDecimal.valueOf(width)).intValue();
            height = dto.getHrScale().multiply(BigDecimal.valueOf(height)).intValue();
        }
        if (dto.getEnableExtra()) {
            width *= dto.getUpscalingResize();
            height *= dto.getUpscalingResize();
        }

        Cmd cmd = RequestContext.CMD_CONTEXT.get();
        Txt2ImgCmd txt2ImgCmd = (Txt2ImgCmd) cmd;
        ImageSizeEnum imageSizeEnum = ImageSizeEnum.getEnumByCode(txt2ImgCmd.getSize());
        ImageQualityEnum imageQualityEnum = ImageQualityEnum.getEnumByCode(txt2ImgCmd.getQuality());

        return Pic.builder()
                .id(IdUtils.nextSnowflakeId())
                .uuid(dto.getResImageUuid())
                .urlUuid(dto.getResImageUrlUuid())
                .userId(task.getUserId())
                .taskId(task.getId())
                .modelId(txt2ImgCmd.getModelId())
                .type(task.getType())
                // Pic保存原始描述词
                .prompt(txt2ImgCmd.getPrompt())
                .negativePrompt(txt2ImgCmd.getNegativePrompt())
                // 翻译后的描述词
                .translatedPrompt(dto.getPrompt())
                .translatedNegativePrompt(dto.getNegativePrompt())
                .quality(imageQualityEnum)
                .ratio(imageSizeEnum.getRatio())
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
                .enableExtra(dto.getEnableExtra())
                .upscalingResize(dto.getUpscalingResize())
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
                .type(task.getType())
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
                .type(task.getType())
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
                .type(task.getType())
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
    }

    private static Pic convertRestoreFace(Task task) {
        RestoreFaceDto dto = JacksonUtils.toBean(task.getBody(), RestoreFaceDto.class);
        LocalDateTime now = task.getCreateTime();

        int width = dto.getWidth();
        int height = dto.getHeight();
        if (dto.getEnableHr()) {
            width = dto.getHrScale().multiply(BigDecimal.valueOf(width)).intValue();
            height = dto.getHrScale().multiply(BigDecimal.valueOf(height)).intValue();
        }
        if (dto.getEnableExtra()) {
            width *= dto.getUpscalingResize();
            height *= dto.getUpscalingResize();
        }

        Pic parentPic = RequestContext.PARENT_PIC_CONTEXT.get();

        return Pic.builder()
                .id(IdUtils.nextSnowflakeId())
                .uuid(dto.getResImageUuid())
                .urlUuid(dto.getResImageUrlUuid())
                .userId(task.getUserId())
                .taskId(task.getId())
                .modelId(parentPic.getModelId())
                .parentPicId(parentPic.getId())
                .type(task.getType())
                .prompt(parentPic.getPrompt())
                .negativePrompt(parentPic.getNegativePrompt())
                .translatedPrompt(parentPic.getTranslatedPrompt())
                .translatedNegativePrompt(parentPic.getTranslatedNegativePrompt())
                .quality(parentPic.getQuality())
                .ratio(parentPic.getRatio())
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
                .enableExtra(dto.getEnableExtra())
                .upscalingResize(dto.getUpscalingResize())
                .createTime(now)
                .updateTime(now)
                .isDelete(0)
                .build();
    }

}
