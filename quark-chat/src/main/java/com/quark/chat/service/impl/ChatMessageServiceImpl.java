package com.quark.chat.service.impl;

import com.quark.chat.service.ChatMessageService;    
import com.quark.common.entity.ChatMessage;
import com.quark.common.dao.ChatMessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageDao repository;

    @Override
    public ChatMessage save(Long fromId, Long toId, String content) {
        ChatMessage msg = new ChatMessage();
        msg.setFromId(fromId);
        msg.setToId(toId);
        msg.setContent(content);
        msg.setInitTime(LocalDateTime.now());
        return repository.save(msg);
    }
}
