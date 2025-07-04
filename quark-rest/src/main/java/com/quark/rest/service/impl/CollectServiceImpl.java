package com.quark.rest.service.impl;

import com.quark.common.dao.CollectDao;
import com.quark.common.dao.PostsDao;
import com.quark.common.dao.UserDao;
import com.quark.common.dto.QuarkResult;
import com.quark.common.entity.Collect;
import com.quark.common.entity.Notification;
import com.quark.common.entity.Posts;
import com.quark.common.entity.User;
import com.quark.rest.service.CollectService;
import com.quark.rest.service.NotificationService;
import com.quark.rest.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollectServiceImpl implements CollectService {

    @Autowired private CollectDao collectDao;
    @Autowired private UserDao    userDao;
    @Autowired private PostsDao   postsDao;
    @Autowired private NotificationService notificationService;
    @Autowired private WebSocketService     webSocketService;

    /** ---------------- 新增收藏 ---------------- */
    @Override
    public QuarkResult collect(Integer userId, Integer postsId) {
        if (collectDao.findByUserIdAndPostsId(userId, postsId) != null)
            return QuarkResult.error("请勿重复收藏");

        User  user  = userDao.findOne(userId);
        Posts posts = postsDao.findOne(postsId);
        Collect collect = new Collect();
        collect.setUser(user);
        collect.setPosts(posts);
        collectDao.save(collect);

        // 更新 quark_posts.collet_num
        posts.setColletNum(posts.getColletNum() + 1);
        postsDao.save(posts);

        // 通知楼主
        if (!posts.getUser().getId().equals(userId)) {
            Notification n = new Notification();
            n.setPosts(posts);
            n.setFromuser(user);
            n.setTouser(posts.getUser());
            n.setInitTime(new Date());
            notificationService.save(n);
            webSocketService.sendToOne(posts.getUser().getId());
        }
        return QuarkResult.ok(null);
    }

    /** ---------------- 取消收藏 ---------------- */
    @Override
    public QuarkResult cancel(Integer userId, Integer postsId) {
        Collect collect = collectDao.findByUserIdAndPostsId(userId, postsId);
        if (collect == null) return QuarkResult.error("未找到收藏记录");
        collectDao.delete(collect);

        Posts posts = postsDao.findOne(postsId);
        int num = posts.getColletNum() == null ? 0 : posts.getColletNum();
        posts.setColletNum(Math.max(0, num - 1));
        postsDao.save(posts);
        return QuarkResult.ok(null);
    }

    /** ---------------- 是否已收藏 ---------------- */
    @Override
    public boolean isCollected(Integer userId, Integer postsId) {
        return collectDao.findByUserIdAndPostsId(userId, postsId) != null;
    }

    /** ---------------- 我的收藏分页 ---------------- */
    @Override
    public Page<Posts> list(Integer userId, int page, int size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "initTime");
        Page<Collect> pageData = collectDao.findByUserId(userId, pageable);
        List<Posts> posts = pageData.getContent()
                .stream()
                .map(Collect::getPosts)
                .collect(Collectors.toList());
        return new PageImpl<>(posts, pageable, pageData.getTotalElements());
    }

    @Override
    public Page<Posts> listCollects(Integer userId, int page, int size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "initTime");
        Page<Collect> pageData = collectDao.findByUserId(userId, pageable);

        List<Posts> posts = pageData.getContent()
                .stream()
                .map(Collect::getPosts)
                .collect(Collectors.toList());

        return new PageImpl<>(posts, pageable, pageData.getTotalElements());
    }

}