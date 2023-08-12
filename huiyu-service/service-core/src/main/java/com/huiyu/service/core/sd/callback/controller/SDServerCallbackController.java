package com.huiyu.service.core.sd.callback.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.huiyu.common.core.result.R;
import com.huiyu.common.core.result.ResultCode;
import com.huiyu.common.core.util.JacksonUtils;
import com.huiyu.service.core.aspect.annotation.MethodMonitor;
import com.huiyu.service.core.constant.HuiyuConstant;
import com.huiyu.service.core.enums.PicStatusEnum;
import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.sd.callback.cmd.UploadSuccessCallbackCmd;
import com.huiyu.service.core.service.PicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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

    private final PicService picService;

    private final WxMaService wxMaService;

    /**
     * 图片上传成功回调
     *
     * @return ok
     */
    @PostMapping("/uploadSuccessCallback")
    @MethodMonitor
    public R<?> uploadSuccessCallback(@RequestBody UploadSuccessCallbackCmd cmd) {
        log.info("图片上传成功回调: {}", JacksonUtils.toJsonStr(cmd));
        if (!HuiyuConstant.callbackToken.equals(cmd.getToken())) {
            return R.create(ResultCode.FORBIDDEN);
        }

        String imgUrl = HuiyuConstant.cdnUrlGen + cmd.getResImageUrlUuid() + HuiyuConstant.imageSuffix;
        log.info("图片上传成功 url: {}", imgUrl);

        PicStatusEnum picStatus = PicStatusEnum.GENERATED;
        try {
            // 减小图片尺寸
            String checkUrl = imgUrl + "!/fw/720";
            boolean res = wxMaService.getSecCheckService().checkImage(checkUrl);
            log.info("图片上传成功, 调用微信图片审核接口, imgUrl: {}, res: {}", res);
        } catch (WxErrorException e) {
            log.error("图片上传成功, 调用微信图片审核接口, imgUrl: {}, 错误信息: {}", e.getMessage());
            picStatus = PicStatusEnum.RISKY;
        }

        // 更新图片状态
        Pic pic = Pic.builder()
                .uuid(cmd.getResImageUuid())
                .path(imgUrl)
                .status(picStatus)
                .updateTime(LocalDateTime.now())
                .build();
        picService.updateByUuid(pic);

        picService.sendMsgByPicGenerated(cmd.getResImageUuid());
        return R.ok();
    }
}
