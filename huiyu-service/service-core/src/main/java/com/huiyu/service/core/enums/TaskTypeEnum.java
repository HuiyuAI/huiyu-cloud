package com.huiyu.service.core.enums;

import com.huiyu.service.core.sd.constant.SDAPIConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 任务类型枚举
 *
 * @author Naccl
 * @date 2023-06-16
 */
@Getter
@ToString
@AllArgsConstructor
public enum TaskTypeEnum implements BaseEnum<String> {

    TXT2IMG("txt2img", "文生图", SDAPIConstant.TXT2IMG),

    IMG2IMG("img2img", "图生图", SDAPIConstant.IMG2IMG),

    UPSCALE("upscale", "细节修复放大", SDAPIConstant.UPSCALE),

    EXTRA("extra", "高清化", SDAPIConstant.EXTRA),

    RESTORE_FACE("restoreFace", "脸部修复", SDAPIConstant.RESTORE_FACE),
    ;

    private String dictKey;

    private String desc;

    private String sdApi;

}
