package com.huiyu.service.core.sd;

import com.huiyu.service.core.model.cmd.ExtraCmd;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;

/**
 * @author: 陈瑾
 * @date: 2023/6/19 13:11
 * @Description: 计算积分
 **/
public class SDPointCalculator {
    public static int calcPointConsume(Txt2ImgCmd cmd) {
        Integer count = cmd.getCount();
        Integer quality = cmd.getQuality();

        // TODO 热配
        int point = count * quality;

        return point;
    }

    public static int calcPointConsume(Img2ImgCmd cmd) {
        return 0;
    }

    public static int calcPointConsume(RestoreFaceCmd cmd) {
        // TODO 热配
        // 根据原图的积分计算
        int point = 2;
        return point;
    }

    public static int calcPointConsume(ExtraCmd cmd) {
        // 固定积分 TODO 热配
        int point = 2;
        return point;
    }

}
