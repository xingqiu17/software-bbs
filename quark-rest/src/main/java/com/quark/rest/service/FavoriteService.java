package com.quark.rest.service;

import com.quark.common.dto.QuarkResult;
import com.quark.common.entity.Posts;
import org.springframework.data.domain.Page;

public interface FavoriteService {
    /** 新增收藏 */
    QuarkResult Favorite(Integer userId, Integer postsId);

    /** 取消收藏 */
    QuarkResult cancelFavorite(Integer userId, Integer postsId);

    /** 是否已收藏 */
    boolean isFavorited(Integer userId, Integer postsId);

}