package com.huiyu.service.core.utils.mirai;

import com.huiyu.service.core.entity.Pic;
import com.huiyu.service.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Naccl
 * @date 2023-08-11
 */
@Slf4j
public class MiraiUtils {

    private static volatile Bot bot;

    private static volatile Group group;

    public static void main(String[] args) {
        long qq = 2106308917L;
        long groupId = 369116826L;

        login(qq, groupId);
    }

    public static synchronized void login(long qq, long groupId) {
        bot = BotFactory.INSTANCE.newBot(qq, BotAuthorization.byQRCode(), configuration -> {
            configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
        });

        bot.login();

        group = bot.getGroup(groupId);

        registerGroupMsgListener(groupId);
    }

    public static synchronized void close() {
        if (bot != null) {
            bot.close();
            bot = null;

            if (group != null) {
                group = null;
            }
        }
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

        String msg = String.format(
                "用户ID: {}\n" +
                        "图片ID: {}\n" +
                        "任务类型: {}\n" +
                        "生成时间: {}\n" +
                        "图片地址: {}\n" +
                        "\n",
                pic.getUserId(), pic.getId(), pic.getType(), pic.getUpdateTime(), pic.getPath()
        );

        log.info(msg);

        sendMsg(msg, pic.getPath());
    }

    private static void registerGroupMsgListener(long groupId) {
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (event.getGroup().getId() == groupId) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("Hi, you just said: '")
                        .append(event.getMessage())
                        .append("'")
                        .build()
                );
            }
        });
    }

    private static boolean checkLogin() {
        if (bot == null || group == null) {
            return false;
        }
        return true;
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
