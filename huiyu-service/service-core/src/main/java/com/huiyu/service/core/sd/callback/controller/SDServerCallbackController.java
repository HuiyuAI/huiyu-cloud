package com.huiyu.service.core.sd.callback.controller;

import com.huiyu.common.core.result.R;
import com.huiyu.common.core.result.ResultCode;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.constant.HuiyuConstant;
import com.huiyu.service.core.sd.callback.cmd.UploadSuccessCallbackCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Stable Diffusion服务端回调
 *
 * @author Naccl
 * @date 2023-06-14
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/callback/sd/")
public class SDServerCallbackController {

    /**
     * 图片上传成功回调
     *
     * @return ok
     */
    @PostMapping("/uploadSuccessCallback")
    public R<?> uploadSuccessCallback(@RequestBody UploadSuccessCallbackCmd cmd) {
        log.info("图片上传成功回调: {}", JacksonUtils.toJsonStr(cmd));
        if (!HuiyuConstant.callbackToken.equals(cmd.getToken())) {
            return R.create(ResultCode.FORBIDDEN);
        }

        String resImageUuid = cmd.getResImageUuid();
        String imgUrl = "https://huiyucdn.naccl.top/gen/" + resImageUuid + ".jpg";
        log.info("图片上传成功 url: {}", imgUrl);

        // TODO 上传回调
        // 1. 更新图片状态


        // 2. 更新用户界面


        return R.ok();
    }
}
