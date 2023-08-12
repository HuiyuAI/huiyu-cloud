package com.huiyu.service.core.utils.mirai;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.auth.QRCodeLoginListener;
import net.mamoe.mirai.network.RetryLaterException;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * 重写扫码登录监听器
 *
 * @author Naccl
 * @date 2023-08-12
 */
@Slf4j
@Getter
class QRCodeLoginListenerImpl implements QRCodeLoginListener {

    private File qrFile = null;

    private QRCodeLoginListener.State status;

    private boolean cancel = false;

    @SneakyThrows
    @Override
    public void onFetchQRCode(@NotNull Bot bot, @NotNull byte[] bytes) {
        if (qrFile == null) {
            qrFile = File.createTempFile("mirai-qrcode", ".png");
            qrFile.deleteOnExit();
        }

        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), "png", qrFile);

        log.info("[QRCodeLogin] 二维码图片文件 {}", qrFile.toPath().toUri());
    }

    @Override
    public void onIntervalLoop() {
        if (cancel) {
            log.info("[QRCodeLogin] 已取消登录");
            throw new RetryLaterException("已取消登录", null, true);
        }
        QRCodeLoginListener.super.onIntervalLoop();
    }

    @Override
    public void onStateChanged(@NotNull Bot bot, @NotNull QRCodeLoginListener.State state) {
        this.status = state;
        switch (state) {
            case WAITING_FOR_SCAN:
                log.info("[QRCodeLogin] 等待扫描二维码中");
                break;
            case WAITING_FOR_CONFIRM:
                log.info("[QRCodeLogin] 二维码已扫描，请在手机QQ确认登录");
                break;
            case CANCELLED:
                log.info("[QRCodeLogin] 扫描后取消了登录，将会重新获取二维码");
                break;
            case TIMEOUT:
                log.info("[QRCodeLogin] 二维码超时，将会重新获取二维码");
                break;
            case CONFIRMED:
                log.info("[QRCodeLogin] 已确认登录");
                if (qrFile != null) {
                    qrFile.delete();
                }
                break;
            default:
                log.info("[QRCodeLogin] 未知状态");
                break;
        }
    }

    public void cancel() {
        this.cancel = true;
    }

}
