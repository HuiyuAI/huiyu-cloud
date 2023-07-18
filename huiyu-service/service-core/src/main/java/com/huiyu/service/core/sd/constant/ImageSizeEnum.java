package com.huiyu.service.core.sd.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

/**
 * @author wAnG
 * @Date 2023-06-16  00:24
 */
@Getter
@ToString
@AllArgsConstructor
public enum ImageSizeEnum {

    PHONE_WALLPAPER(1, "手机壁纸", "9:16", 540, 960),
    COMPUTER_WALLPAPER(2, "电脑壁纸", "16:9", 960, 540),
    SOCIAL_MEDIA(3, "社交媒体", "2:3", 512, 768),
    ARTICLE_IMAGE(4, "文章配图", "3:2", 768, 512),
    AVATAR(5, "头像", "1:1", 512, 512),
    UNKNOWN(100, "未知", "", 0, 0);


    private Integer code;

    private String desc;

    private String ratio;

    private Integer width;

    private Integer height;

    public static ImageSizeEnum getEnumByCode(Integer code) {
        return Arrays.stream(ImageSizeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(ImageSizeEnum.UNKNOWN);
    }

    public static ImageSizeEnum getEnumByRatio(String ratio) {
        return Arrays.stream(ImageSizeEnum.values()).filter(e -> e.getRatio().equals(ratio)).findFirst().orElse(ImageSizeEnum.UNKNOWN);
    }
}
