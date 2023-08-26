package com.huiyu.service.core.service.extension;

import cn.binarywang.wx.miniapp.api.WxMaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

/**
 * @author Naccl
 * @date 2023-08-27
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SecureCheckService {

    private static final int ERROR_CODE = 87014;

    private final WxMaService wxMaService;

    /**
     * 微信文本合规检测
     */
    public boolean checkMessage(String message) {
        boolean res = true;
        try {
            log.info("调用微信文本审核接口, message: {}", message);
            res = wxMaService.getSecCheckService().checkMessage(message);
            log.info("调用微信文本审核接口, message: {}, res: {}", message, res);
        } catch (WxErrorException e) {
            log.warn("调用微信文本审核接口, message: {}, 错误信息: {}", message, e.getMessage());
            if (e.getError().getErrorCode() == ERROR_CODE) {
                res = false;
            }
        }
        return res;
    }

    /**
     * 微信图片合规检测
     */
    public boolean checkImage(String imgUrl) {
        boolean res = true;
        try {
            log.info("调用微信图片审核接口, imgUrl: {}", imgUrl);
            res = wxMaService.getSecCheckService().checkImage(imgUrl);
            log.info("调用微信图片审核接口, imgUrl: {}, res: {}", imgUrl, res);
        } catch (WxErrorException e) {
            log.warn("调用微信图片审核接口, imgUrl: {}, 错误信息: {}", imgUrl, e.getMessage());
            if (e.getError().getErrorCode() == ERROR_CODE) {
                res = false;
            }
        }
        return res;
    }
}
