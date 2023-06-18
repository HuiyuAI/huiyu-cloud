package com.huiyu.service.core.constant;

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

    TXT2IMG("txt2img", "文生图"),

    IMG2IMG("img2img", "图生图"),

    UPSCALE("upscale", "细节修复放大"),

    EXTRA("extra", "高清化"),
    ;

    private String dictKey;

    private String desc;

}
