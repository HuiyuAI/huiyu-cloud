package com.huiyu.service.core.sd;

import com.huiyu.common.web.util.JwtUtils;
import com.huiyu.service.core.model.cmd.Cmd;
import com.huiyu.service.core.model.cmd.Txt2ImgCmd;

/**
 * @author: 陈瑾
 * @date: 2023/6/19 13:11
 * @Description: 计算积分
 **/
public class SDCmdCountIntegral {
    public static int countByCmd(Cmd cmd) {
        // todo 根据cmd各个参数计算积分
        int integral = 0;
        if (cmd instanceof Txt2ImgCmd) {
            Txt2ImgCmd txt2ImgCmd = (Txt2ImgCmd) cmd;
            Integer quality = txt2ImgCmd.getQuality();
            integral += quality;
        }

        // todo 根据用户角色或者用户等级 打折 积分
        String role = JwtUtils.getRole();
        if ("ROLE_ROOT".equals(role)) {
            integral *= 0;
        }
        return -1;
    }

}
