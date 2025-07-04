package com.quark.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quark.common.utils.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**

 *
 * 帖子
 */
@Entity
@Table(name = "quark_posts")
public class Posts implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    //与标签的关系
    @ManyToOne
    @JoinColumn(nullable = false, name = "label_id")
    private Label label;

    //标题
    @Column(unique = true, nullable = false)
    private String title;

    //内容
    @Column(columnDefinition = "text")
    private String content;

    //发布时间
    @Column(nullable = false)
    @JsonFormat(pattern = Constants.DATETIME_FORMAT, timezone = "GMT+8")
    private Date initTime;

    //是否置顶
    private boolean top;

    //是否精华
    private boolean good;

    //与用户的关联关系
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    /** 评论数，对应表字段 reply_count */
    //回复数量
    @Column(name = "reply_count")
    private int replyCount = 0;

    /** 收藏数，表字段是 collet_num */
    @Column(name = "collet_num")
    private Integer colletNum = 0;
    public Integer getColletNum() { return colletNum; }
    public void setColletNum(Integer colletNum) { this.colletNum = colletNum; }

    /** 点赞数，对应表字段 like_num */
    @Column(name = "like_num")
    private Integer likeNum = 0;
    public Integer getLikeNum() { return likeNum; }
    public void setLikeNum(Integer likeNum) { this.likeNum = likeNum; }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Boolean getGood() {
        return good;
    }

    public void setGood(Boolean good) {
        this.good = good;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", label=" + label +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", initTime=" + initTime +
                ", top=" + top +
                ", good=" + good +
                ", user=" + user +
                ", replyCount=" + replyCount +
                '}';
    }
}
