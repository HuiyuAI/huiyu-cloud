package com.huiyu.service.core.utils.mirai;

import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.exception.BizException;
import com.huiyu.service.core.model.vo.MiraiStatusVo;
import com.huiyu.service.core.service.PicService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.auth.QRCodeLoginListener;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * 单机版QQ机器人
 *
 * @author Naccl
 * @date 2023-08-11
 */
@Slf4j
@Component
public class MiraiUtils {

    private static PicService picService;

    private static volatile Bot bot;

    private static volatile Group group;

    private static volatile CustomLoginSolver customLoginSolver;

    private static volatile boolean isLoggingIn = false;

    @Getter
    private static volatile Long qq;

    @Getter
    private static volatile Long groupId;

    public MiraiUtils(PicService picService) {
        MiraiUtils.picService = picService;
    }

    public static synchronized void login(long qq, long groupId) {
        if (MiraiUtils.qq != null) {
            // 已经登录
            return;
        }

        isLoggingIn = true;

        customLoginSolver = new CustomLoginSolver();

        bot = BotFactory.INSTANCE.newBot(qq, BotAuthorization.byQRCode(), configuration -> {
            configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
            configuration.fileBasedDeviceInfo();
            configuration.setLoginSolver(customLoginSolver);
        });

        bot.login();

        MiraiUtils.qq = qq;
        MiraiUtils.groupId = groupId;
        isLoggingIn = false;

        group = bot.getGroup(groupId);

        registerGroupMsgListener(groupId);
    }

    public static void close() {
        if (isLoggingIn) {
            customLoginSolver.getQrCodeLoginListener().cancel();
            isLoggingIn = false;
            bot = null;
        } else if (bot != null) {
            bot.close();
            bot = null;
        }

        group = null;
        customLoginSolver = null;
        qq = null;
        groupId = null;
    }

    public static MiraiStatusVo getStatus() throws IOException {
        if (!isLoggingIn && customLoginSolver == null) {
            return new MiraiStatusVo("CANCEL", null);
        }
        if (customLoginSolver == null || customLoginSolver.getQrCodeLoginListener() == null) {
            return new MiraiStatusVo(null, null);
        }

        QRCodeLoginListenerImpl qrCodeLoginListener = customLoginSolver.getQrCodeLoginListener();

        if (qrCodeLoginListener.getStatus() == null) {
            return new MiraiStatusVo(null, null);
        }

        if (qrCodeLoginListener.getStatus() == QRCodeLoginListener.State.CONFIRMED) {
            return new MiraiStatusVo(String.valueOf(QRCodeLoginListener.State.CONFIRMED), null);
        }

        File qrFile = qrCodeLoginListener.getQrFile();
        byte[] bytes = Files.readAllBytes(Paths.get(qrFile.getPath()));
        String base64 = Base64.getEncoder().encodeToString(bytes);

        return new MiraiStatusVo(String.valueOf(qrCodeLoginListener.getStatus()), base64);
    }

    public static void sendMsg(String msg) {
        if (!checkLogin()) {
            return;
        }

        Message message = new PlainText(msg);

        group.sendMessage(message);
    }

    public static void sendMsg(String msg, String imgUrl) {
        if (!checkLogin()) {
            return;
        }

        Image image = getImageByUrl(imgUrl);

        Message message = new PlainText(msg).plus(image);

        group.sendMessage(message);
    }

    public static void sendMsgByPicGenerated(Pic pic) {
        if (!checkLogin()) {
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String msg = String.format(
                "用户ID: %s\n" +
                        "图片ID: %s\n" +
                        "任务类型: %s\n" +
                        "创建时间: %s\n" +
                        "更新时间: %s\n" +
                        "图片地址: %s\n",
                pic.getUserId(),
                pic.getId(),
                pic.getType().getDesc(),
                pic.getCreateTime().format(dateTimeFormatter),
                pic.getUpdateTime().format(dateTimeFormatter),
                pic.getPath()
        );

        log.info(msg);

        sendMsg(msg, pic.getPath());
    }

    private static void registerGroupMsgListener(long groupId) {
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (event.getGroup().getId() == groupId) {
                MessageChain messageChain = event.getMessage();
                log.info("[机器人收到消息]:{}", messageChain.contentToString());
                // 判断是不是at机器人
                At at = (At) messageChain.stream().filter(m -> m instanceof At).findAny().orElse(null);
                boolean pending = at != null && at.getTarget() == bot.getId();
                if (!pending) {
                    return;
                }

                // 找到引用的消息
                QuoteReply quote = messageChain.get(QuoteReply.Key);
                if (quote == null) {
                    return;
                }
                // 获取引用的原始消息
                MessageChain quoteChain = quote.getSource().getOriginalMessage();

                // 获取原始消息中的图片ID
                SingleMessage singleMessage = quoteChain.stream().findFirst().orElse(null);
                if (singleMessage == null) {
                    return;
                }
                String msgContent = singleMessage.contentToString();
                String picIdStr = StringUtils.substringBetween(msgContent, "图片ID: ", "\n");
                if (StringUtils.isEmpty(picIdStr)) {
                    return;
                }
                Long picId = Long.valueOf(picIdStr);

                Pic pic = picService.getById(picId);
                if (pic == null) {
                    return;
                }

                PlainText plainText = (PlainText) messageChain.stream().filter(m -> m instanceof PlainText).findFirst().orElse(null);
                if (plainText == null) {
                    return;
                }

                if ("pass".equals(plainText.contentToString().trim())) {
                    event.getSubject().sendMessage(new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append(String.format("图片ID: %s已通过审核", picId))
                            .build()
                    );
                }
                if ("ban".equals(plainText.contentToString().trim())) {
                    event.getSubject().sendMessage(new MessageChainBuilder()
                            .append(new QuoteReply(event.getMessage()))
                            .append(String.format("图片ID: %s已拒绝发布", picId))
                            .build()
                    );
                }
            }
        });
    }

    private static boolean checkLogin() {
        return bot != null && group != null;
    }

    private static Image getImageByUrl(String imgUrl) {
        Image image;
        InputStream inputStream = null;

        try {
            inputStream = new URL(imgUrl).openStream();
            image = Contact.uploadImage(group, inputStream);
        } catch (IOException e) {
            log.error("mirai图片上传失败, imgUrl: {}", imgUrl, e);
            throw new BizException("mirai图片上传失败");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("inputStream关闭失败", e);
            }
        }
        return image;
    }
}
