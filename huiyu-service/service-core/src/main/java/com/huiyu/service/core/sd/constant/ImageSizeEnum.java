package com.huiyu.service.core.sd.constant;

import java.util.Objects;

/**
 * @author wAnG
 * @Date 2023-06-16  00:24
 */
public enum ImageSizeEnum {

    AVATAR_FRAME(1, "头像框", "1:1", 512, 512),
    SOCIAL_MEDIA(2, "社交媒体", "3:4", 576, 768),
    ARTICLE_IMAGE(3, "文章配图", "4:3", 768, 576),
    PHONE_WALLPAPER(4, "手机壁纸", "9:16", 1080, 1920),
    COMPUTER_WALLPAPER(5, "电脑壁纸", "16:9", 1920, 1080),
    UNKNOWN(100, "未知", "", 0, 0);


    private Integer code;

    private String desc;

    private String proportion;

    private Integer wight;

    private Integer high;

    ImageSizeEnum(Integer code, String desc, String proportion, Integer wight, Integer high) {
        this.code = code;
        this.desc = desc;
        this.proportion = proportion;
        this.wight = wight;
        this.high = high;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public Integer getWight() {
        return wight;
    }

    public void setWight(Integer wight) {
        this.wight = wight;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public static ImageSizeEnum getEnumByCode(Integer code) {
        for (ImageSizeEnum sizeEnums : ImageSizeEnum.values()) {
            if (Objects.equals(sizeEnums.code, code)) {
                return sizeEnums;
            }
        }
        return ImageSizeEnum.UNKNOWN;
    }
}
