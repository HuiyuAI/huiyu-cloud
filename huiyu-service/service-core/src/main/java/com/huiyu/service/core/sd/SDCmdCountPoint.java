package com.huiyu.service.core.sd;

import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.model.cmd.Img2ImgCmd;
import com.huiyu.service.core.model.cmd.RestoreFaceCmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;

/**
 * @author: 陈瑾
 * @date: 2023/6/19 13:11
 * @Description: 计算积分
 **/
public class SDCmdCountPoint {
    public static int calcPointConsume(Cmd cmd) {
        if (cmd instanceof Txt2ImgCmd) {
            return calcTxt2ImgPointConsume((Txt2ImgCmd) cmd);
        } else if (cmd instanceof Img2ImgCmd) {
            return calcImg2ImgPointConsume((Img2ImgCmd) cmd);
        } else if (cmd instanceof RestoreFaceCmd) {
            return calcRestoreFacePointConsume((RestoreFaceCmd) cmd);
        }
        return 0;
    }

    public static int calcTxt2ImgPointConsume(Txt2ImgCmd cmd) {
        Integer count = cmd.getCount();
        Integer quality = cmd.getQuality();

        // TODO 热配
        int point = count * quality;

        return point;
    }

    private static int calcImg2ImgPointConsume(Img2ImgCmd cmd) {
        return 0;
    }

    private static int calcRestoreFacePointConsume(RestoreFaceCmd cmd) {
        // TODO 热配
        // 根据原图的积分计算
        int point = 2;
        return point;
    }

}
