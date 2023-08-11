package com.huiyu.service.core.utils.mirai;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

/**
 * @author Naccl
 * @date 2023-08-11
 */
public class MiraiUtils {

    public static void main(String[] args) {
        Bot bot = BotFactory.INSTANCE.newBot(123456, "test");
        bot.login();

        afterLogin(bot);
    }

    public static void afterLogin(Bot bot) {
        long yourQQNumber = 123456789;
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, (event) -> {
            if (event.getSender().getId() == yourQQNumber) {
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
}
