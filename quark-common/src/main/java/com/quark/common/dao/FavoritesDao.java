package com.quark.common.dao;

import com.quark.common.entity.Favorites;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesDao extends JpaRepository<Favorites, Integer> {
    Favorites findByUserIdAndPostsId(Integer userId, Integer postsId);

    Page<Favorites> findByUserId(Integer userId, Pageable pageable);

    Long countByPostsId(Integer postsId);
}