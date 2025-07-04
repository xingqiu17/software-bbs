package com.quark.rest.controller;

import com.quark.common.base.BaseController;
import com.quark.common.dto.QuarkResult;
import com.quark.common.entity.Posts;
import com.quark.rest.service.CollectService;
import com.quark.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collects")
public class CollectController extends BaseController {

    @Autowired private CollectService collectService;
    @Autowired private UserService    userService;

    // 收藏
    @PostMapping
    public QuarkResult collect(@RequestParam Integer postsId, @RequestParam String token){
        Integer uid = userService.getUserByToken(token).getId();
        return collectService.collect(uid, postsId);
    }

    // 取消收藏
    @DeleteMapping
    public QuarkResult cancel(@RequestParam Integer postsId, @RequestParam String token){
        Integer uid = userService.getUserByToken(token).getId();
        return collectService.cancel(uid, postsId);
    }

    /** 我的收藏列表 (★ 新增) */
    @GetMapping
    public QuarkResult list(@RequestParam String token,
                            @RequestParam(defaultValue="0") int page,
                            @RequestParam(defaultValue="10") int size){
        Integer uid = userService.getUserByToken(token).getId();
        Page<Posts> p = collectService.listCollects(uid, page, size);
        return QuarkResult.ok(p.getContent(), p.getTotalElements(), p.getNumberOfElements());
    }

}