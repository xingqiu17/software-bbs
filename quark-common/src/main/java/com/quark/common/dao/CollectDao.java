package com.quark.common.dao;

import com.quark.common.entity.Collect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectDao extends JpaRepository<Collect, Integer> {
    Collect findByUserIdAndPostsId(Integer userId, Integer postsId);

    Page<Collect> findByUserId(Integer userId, Pageable pageable);

    Long countByPostsId(Integer postsId);
}