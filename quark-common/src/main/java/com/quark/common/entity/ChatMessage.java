package com.quark.common.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 私聊消息实体，对应数据库表 chat_message
 */
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 发送者用户 ID */
    @Column(name = "from_id", nullable = false)
    private Long fromId;

    /** 接收者用户 ID */
    @Column(name = "to_id", nullable = false)
    private Long toId;

    /** 发送时间 */
    @Column(name = "init_time", nullable = false)
    private LocalDateTime initTime;

    /** 消息内容 */
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    public ChatMessage() {
        // JPA 需要无参构造器
    }

    public ChatMessage(Long fromId, Long toId, LocalDateTime initTime, String content) {
        this.fromId = fromId;
        this.toId = toId;
        this.initTime = initTime;
        this.content = content;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public LocalDateTime getInitTime() {
        return initTime;
    }

    public void setInitTime(LocalDateTime initTime) {
        this.initTime = initTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // --- equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatMessage)) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(fromId, that.fromId) &&
               Objects.equals(toId, that.toId) &&
               Objects.equals(initTime, that.initTime) &&
               Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromId, toId, initTime, content);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
               "id=" + id +
               ", fromId=" + fromId +
               ", toId=" + toId +
               ", initTime=" + initTime +
               ", content='" + content + '\'' +
               '}';
    }
}
