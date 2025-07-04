package com.quark.rest.controller;

import com.quark.common.base.BaseController;
import com.quark.common.dto.QuarkResult;
import com.quark.rest.service.FavoriteService;
import com.quark.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController extends BaseController {

    @Autowired private FavoriteService FavoriteService;
    @Autowired private UserService    userService;

    // 点赞
    @PostMapping
    public QuarkResult Favorite(@RequestParam Integer postsId, @RequestParam String token){
        Integer uid = userService.getUserByToken(token).getId();
        return FavoriteService.Favorite(uid, postsId);
    }

    // 取消点赞
    @DeleteMapping
    public QuarkResult cancelFavorite(@RequestParam Integer postsId, @RequestParam String token){
        Integer uid = userService.getUserByToken(token).getId();
        return FavoriteService.cancelFavorite(uid, postsId);
    }


}