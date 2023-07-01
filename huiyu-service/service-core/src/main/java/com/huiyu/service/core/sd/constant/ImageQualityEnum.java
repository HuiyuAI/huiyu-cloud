package com.huiyu.service.core.sd.constant;

import com.huiyu.service.core.constant.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Naccl
 * @date 2023-07-01
 */
@Getter
@ToString
@AllArgsConstructor
public enum ImageQualityEnum implements BaseEnum<Integer> {

    HD(1, "高清", false, BigDecimal.ZERO, BigDecimal.ZERO, false, 0),
    UHD(2, "超清", true, new BigDecimal("0.6"), BigDecimal.valueOf(2), false, 0),
    UHD4K(3, "超高清4K", true, new BigDecimal("0.6"), BigDecimal.valueOf(2), true, 2),
    UNKNOWN(100, "未知", false, BigDecimal.ZERO, BigDecimal.ZERO, false, 0),
    ;

    private Integer dictKey;

    private String desc;
    /**
     * 是否启用高分辨率修复
     */
    private Boolean enableHr;
    /**
     * 重绘强度 0.00-1.00之间两位小数
     */
    private BigDecimal denoisingStrength;
    /**
     * 放大倍数 1-4之间两位小数 步进0.05
     */
    private BigDecimal hrScale;
    /**
     * 是否启用工序三高清化extra
     */
    private Boolean enableExtra;
    /**
     * 高清化extra放大倍数 1-4
     */
    private Integer upscalingResize;

    public static ImageQualityEnum getEnumByCode(Integer code) {
        for (ImageQualityEnum qualityEnum : ImageQualityEnum.values()) {
            if (Objects.equals(qualityEnum.dictKey, code)) {
                return qualityEnum;
            }
        }
        return ImageQualityEnum.UNKNOWN;
    }
}
