package com.quark.chat.service;

import com.quark.common.base.BaseService;
import com.quark.common.entity.User;

/**

 */
public interface ChatService extends BaseService<User>{

    /**
     * 根据Token获取用户
     * @param token
     * @return
     */
    User getUserByToken(String token);

    /**
     * 验证用户
     * @param id
     * @return
     */
    boolean authUser(Integer id);
}
