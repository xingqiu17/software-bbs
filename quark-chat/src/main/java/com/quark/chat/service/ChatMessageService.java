package com.quark.chat.service;

import com.quark.common.entity.ChatMessage;

/**
 * 私聊消息服务接口
 */
public interface ChatMessageService {

    /**
     * 保存一条私聊消息
     *
     * @param fromId  发送者 ID
     * @param toId    接收者 ID
     * @param content 消息内容（文本或图片<img>标签）
     * @return 保存后的 ChatMessage 实体
     */
    ChatMessage save(Long fromId, Long toId, String content);

}
