package com.huiyu.service.core.sd.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Naccl
 * @date 2023-07-01
 */
@Getter
@ToString
@AllArgsConstructor
public enum ImageQualityEnum {

    HD(1, "高清", false, false),
    UHD(2, "超清", true, false),
    UHD4K(3, "超高清4K", true, true),
    UNKNOWN(100, "未知", false, false),
    ;

    private Integer code;

    private String desc;
    /**
     * 是否启用高分辨率修复
     */
    private Boolean enableHr;
    /**
     * 是否启用工序三：高清化extra
     */
    private Boolean enableExtra;

    public static ImageQualityEnum getEnumByCode(Integer code) {
        for (ImageQualityEnum qualityEnum : ImageQualityEnum.values()) {
            if (Objects.equals(qualityEnum.code, code)) {
                return qualityEnum;
            }
        }
        return ImageQualityEnum.UNKNOWN;
    }
}
