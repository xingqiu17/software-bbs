package com.quark.common.dao;

import com.quark.common.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface ChatMessageDao 
        extends JpaRepository<ChatMessage, Long>,
                JpaSpecificationExecutor<ChatMessage> {

    /**
     * 查询两人之间的聊天记录（双向）
     */
    List<ChatMessage> findByFromIdAndToIdOrderByInitTimeAsc(Long fromId, Long toId);

    List<ChatMessage> findByToIdAndFromIdOrderByInitTimeAsc(Long toId, Long fromId);
}
