package com.quark.chat.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.quark.chat.entity.ChatUser;
import com.quark.chat.protocol.QuarkChatProtocol;
import com.quark.chat.protocol.QuarkChatType;
import com.quark.chat.protocol.QuarkClientProtocol;
import com.quark.chat.service.ChannelManager;
import com.quark.chat.service.ChatMessageService;
import com.quark.common.entity.ChatMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 消息处理 Handler
 */
@ChannelHandler.Sharable
@Scope("prototype")
@Component
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private ChannelManager manager;

    @Autowired
    private ChatMessageService chatMessageService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        String raw = frame.text();
        logger.debug("【DEBUG】收到原始消息: {}", raw);
        // ➤ 1. 发系统调试消息给自己
        ctx.channel().writeAndFlush(
            new TextWebSocketFrame(
                QuarkChatProtocol.buildSysMessage("[DEBUG] raw=" + raw)
            )
        );

        ChatUser chatUser = manager.getChatUser(ctx.channel());
        if (chatUser == null || !chatUser.isAuth()) {
            logger.warn("【DEBUG】未认证或用户不存在，忽略消息");
            ctx.channel().writeAndFlush(
                new TextWebSocketFrame(
                    QuarkChatProtocol.buildSysMessage("[DEBUG] 未认证或用户不存在")
                )
            );
            return;
        }

        QuarkClientProtocol clientProto = JSON.parseObject(
            raw, new TypeReference<QuarkClientProtocol>() {});
        byte msgType = clientProto.getType();
        Long fromId  = chatUser.getUser().getId().longValue();
        Long toId    = clientProto.getReceiverId();
        String content = clientProto.getMsg();

        logger.debug("【DEBUG】解析后 -> type={}, fromId={}, toId={}, msg={}",
                    msgType, fromId, toId, content);
        // ➤ 2. 同样把解析结果也发给自己
        manager.sendToUser(fromId,
            QuarkChatProtocol.buildSysMessage(
                "[DEBUG] parsed -> type=" + msgType
            + ", from=" + fromId
            + ", to=" + toId
            + ", msg=" + content
            )
        );

        if (msgType == QuarkChatType.PRIVATE_MESSAGE_CODE) {
            logger.debug("【DEBUG】进入私聊分支，准备存库");
            manager.sendToUser(fromId,
                QuarkChatProtocol.buildSysMessage("[DEBUG] 私聊分支, 存库中…")
            );

            ChatMessage saved = chatMessageService.save(fromId, toId, content);
            logger.debug("【DEBUG】消息已存库, id={}", saved.getId());
            manager.sendToUser(fromId,
                QuarkChatProtocol.buildSysMessage("[DEBUG] 存库完成, ID=" + saved.getId())
            );

            String msgCode = QuarkChatProtocol.buildMessageCode(chatUser.getUser(), content);
            logger.debug("【DEBUG】构造推送协议: {}", msgCode);
            manager.sendToUser(fromId,
                QuarkChatProtocol.buildSysMessage("[DEBUG] 推送协议构造完毕")
            );

            manager.sendToUser(toId,   msgCode);
            manager.sendToUser(fromId, msgCode);

        } else {
            logger.debug("【DEBUG】进入广播分支");
            manager.sendToUser(fromId,
                QuarkChatProtocol.buildSysMessage("[DEBUG] 广播分支")
            );
            String msgCode = QuarkChatProtocol.buildMessageCode(chatUser.getUser(), content);
            manager.broadMessage(msgCode);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        manager.removeChannel(ctx.channel());
        manager.broadMessage(QuarkChatProtocol.buildSysUserInfo(manager.getUsers()));
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("connection error and close the channel:{}", cause);
        manager.removeChannel(ctx.channel());
        manager.broadMessage(QuarkChatProtocol.buildSysUserInfo(manager.getUsers()));
    }
}
