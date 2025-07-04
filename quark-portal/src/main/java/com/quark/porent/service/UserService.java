package com.quark.porent.service;

import com.quark.porent.entity.User;


public interface UserService {

    User getUserByApi(String token);

    
}
