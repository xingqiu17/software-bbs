package com.quark.rest.service.impl;

import com.quark.common.dao.FavoritesDao;
import com.quark.common.dao.PostsDao;
import com.quark.common.dao.UserDao;
import com.quark.common.dto.QuarkResult;
import com.quark.common.entity.Favorites;
import com.quark.common.entity.Notification;
import com.quark.common.entity.Posts;
import com.quark.common.entity.User;
import com.quark.rest.service.FavoriteService;
import com.quark.rest.service.NotificationService;
import com.quark.rest.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired private FavoritesDao FavoriteDao;
    @Autowired private UserDao    userDao;
    @Autowired private PostsDao   postsDao;
    @Autowired private NotificationService notificationService;
    @Autowired private WebSocketService     webSocketService;

    /** ---------------- 新增点赞 ---------------- */
    @Override
    public QuarkResult Favorite(Integer userId, Integer postsId) {
        if (FavoriteDao.findByUserIdAndPostsId(userId, postsId) != null)
            return QuarkResult.error("请勿重复点赞");

        User  user  = userDao.findOne(userId);
        Posts posts = postsDao.findOne(postsId);
        Favorites Favorite = new Favorites();
        Favorite.setUser(user);
        Favorite.setPosts(posts);
        FavoriteDao.save(Favorite);

        // 更新 quark_posts.Favorite_num
        posts.setLikeNum(posts.getLikeNum() + 1);
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

    /** ---------------- 取消点赞 ---------------- */
    @Override
    public QuarkResult cancelFavorite(Integer userId, Integer postsId) {
        Favorites Favorite = FavoriteDao.findByUserIdAndPostsId(userId, postsId);
        if (Favorite == null) return QuarkResult.error("未找到点赞记录");
        FavoriteDao.delete(Favorite);

        Posts posts = postsDao.findOne(postsId);
        int num = posts.getLikeNum() == null ? 0 : posts.getLikeNum();
        posts.setLikeNum(Math.max(0, num - 1));
        postsDao.save(posts);
        return QuarkResult.ok(null);
    }

    /** ---------------- 是否已点赞 ---------------- */
    @Override
    public boolean isFavorited(Integer userId, Integer postsId) {
        return FavoriteDao.findByUserIdAndPostsId(userId, postsId) != null;
    }

}