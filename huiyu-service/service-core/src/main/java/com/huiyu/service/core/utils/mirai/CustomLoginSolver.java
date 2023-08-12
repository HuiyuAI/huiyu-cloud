package com.huiyu.service.core.utils.mirai;

import kotlin.coroutines.Continuation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.auth.QRCodeLoginListener;
import net.mamoe.mirai.utils.LoginSolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义登录器
 *
 * @author Naccl
 * @date 2023-08-12
 */
@Slf4j
public class CustomLoginSolver extends LoginSolver {

    @Getter
    private QRCodeLoginListenerImpl qrCodeLoginListener;

    @Nullable
    @Override
    public Object onSolvePicCaptcha(@NotNull Bot bot, @NotNull byte[] bytes, @NotNull Continuation<? super String> continuation) {
        return null;
    }

    @Nullable
    @Override
    public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
        return null;
    }

    @NotNull
    @Override
    public QRCodeLoginListener createQRCodeLoginListener(@NotNull Bot bot) {
        qrCodeLoginListener = new QRCodeLoginListenerImpl();
        return qrCodeLoginListener;
    }

}
