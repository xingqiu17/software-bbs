package com.quark.porent.controller;  // 确认这个包名和你的模块路径对应

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @GetMapping("/private")
    public String privateChat(@RequestParam("to") Long toUserId,
                              Model model) {
        // 只把对方 ID 传到模板
        model.addAttribute("toUserId", toUserId);
        return "chat/private-chat";
    }
}
