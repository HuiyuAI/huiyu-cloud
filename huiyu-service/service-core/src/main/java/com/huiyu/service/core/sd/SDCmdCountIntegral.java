package com.huiyu.service.core.sd;

import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;

/**
 * @author: 陈瑾
 * @date: 2023/6/19 13:11
 * @Description: 计算积分
 **/
public class SDCmdCountIntegral {
    public static int calcIntegralConsume(Cmd cmd) {
        if (cmd instanceof Txt2ImgCmd) {
            return calcTxt2ImgIntegralConsume((Txt2ImgCmd) cmd);
        } else if (cmd instanceof Img2ImgCmd) {
            return calcImg2ImgIntegralConsume((Img2ImgCmd) cmd);
        }
        return 0;
    }

    public static int calcTxt2ImgIntegralConsume(Txt2ImgCmd cmd) {
        Integer count = cmd.getCount();
        Integer quality = cmd.getQuality();

        // TODO
        int integral = count * quality;

        return integral;
    }

    private static int calcImg2ImgIntegralConsume(Img2ImgCmd cmd) {
        return 0;
    }

}
