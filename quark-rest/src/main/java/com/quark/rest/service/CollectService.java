package com.quark.rest.service;

import com.quark.common.dto.QuarkResult;
import com.quark.common.entity.Posts;
import org.springframework.data.domain.Page;

public interface CollectService {
    /** 新增收藏 */
    QuarkResult collect(Integer userId, Integer postsId);

    /** 取消收藏 */
    QuarkResult cancel(Integer userId, Integer postsId);

    /** 是否已收藏 */
    boolean isCollected(Integer userId, Integer postsId);

    /** 我的收藏分页 */
    Page<Posts> list(Integer userId, int page, int size);

    Page<Posts> listCollects(Integer uid, int page, int size);
}