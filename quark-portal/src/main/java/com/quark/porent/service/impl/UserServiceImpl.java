package com.quark.porent.service.impl;

import com.quark.porent.entity.QuarkResult;
import com.quark.porent.entity.User;
import com.quark.porent.service.UserService;
import com.quark.porent.utils.HttpClientUtils;
import com.quark.porent.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author LHR
 * Create By 2017/8/24
 */
@Service
public class UserServiceImpl implements UserService{

    @Value("${api_getUserByToken}")
    private String api_getUserByToken;

    @Override
    public User getUserByApi(String token) {
        try {
            String s = HttpClientUtils.doGet(api_getUserByToken + token);
            if (s == null || s.isEmpty()) {
                // 你也可以直接 return null
                return null;
            }
            QuarkResult quarkResult = JsonUtils.jsonToQuarkResult(s, User.class);
            if (quarkResult == null || quarkResult.getData() == null) {
                return null;
            }
            return (User) quarkResult.getData();
        } catch (Exception e) {
            // 可以打日志
            // log.error("getUserByApi error", e);
            return null;
        }
    }

}
