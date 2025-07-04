package com.quark.porent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.quark.porent.dto.EmailRequest;
import com.quark.porent.dto.CodeRequest;
import com.quark.porent.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping("/user")
@Controller
public class UserController {


    @RequestMapping("/login")
    public String login() {
        return "user/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "user/register";
    }

    @RequestMapping("/home")
    public String home() {
        return "user/home";
    }

    @RequestMapping("/set")
    public String setting() {
        return "user/set";
    }

    @RequestMapping("/seticon")
    public String seticon() {
        return "user/seticon";
    }

    @RequestMapping("/setpsw")
    public String setpsw() {
        return "user/setpsw";
    }

    @RequestMapping("/message")
    public String message() { return "user/message"; }

    @RequestMapping("/setschool")
    public String setschool() { return "user/setschool"; }

    @RequestMapping("/mycollects")
    public String mycollects() { return "user/mycollects"; }


    @Autowired
    private MailService mailService;

    @PostMapping("/send-code")
    @ResponseBody
    public String sendCode(@RequestBody EmailRequest emailRequest, HttpSession session) {
        String email = emailRequest.getEmail();
        System.out.println("发送验证码到: " + email);

        // 定义学校映射，key 是邮箱中会出现的学校关键字，value 是学校名称
        Map<String, String> schoolMap = new HashMap<>();
        schoolMap.put(".bjut.", "北京工业大学");
        schoolMap.put(".pku.", "北京大学");
        schoolMap.put(".tsinghua.", "清华大学");
        schoolMap.put(".hit.", "哈尔滨工业大学");
        // TODO: 按需添加更多学校

        // 根据邮箱检查匹配学校
        String matchedSchool = null;
        for (Map.Entry<String, String> entry : schoolMap.entrySet()) {
            if (email.contains(entry.getKey())) {
                matchedSchool = entry.getValue();
                break;
            }
        }

        if (matchedSchool == null) {
            // 没有匹配到学校，拒绝发送验证码
            return "{\"success\":false,\"message\":\"暂无该学校信息，请联系管理员进行设置\"}";
        }

        // 生成6位随机验证码
        String code = String.valueOf(100000 + new Random().nextInt(900000));
        System.out.println("生成验证码: " + code);

        try {
            // 发送邮件验证码
            mailService.send(email, code);

            // 保存验证码、邮箱和学校信息到session，方便后续验证和更新学校字段
            session.setAttribute("emailCode", code);
            session.setAttribute("emailAddress", email);
            session.setAttribute("emailSchool", matchedSchool);

            return "{\"success\":true,\"message\":\"验证码已发送\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"邮件发送失败，请稍后重试\"}";
        }
    }

    @PostMapping("/confirm-code")
    @ResponseBody
    public String confirmCode(@RequestBody CodeRequest codeRequest, HttpSession session) {
        String inputCode = codeRequest.getCode();
        String savedCode = (String) session.getAttribute("emailCode");
        String savedEmail = (String) session.getAttribute("emailAddress");
        String savedSchool = (String) session.getAttribute("emailSchool");

        if (savedCode == null || savedEmail == null || savedSchool == null) {
            return "{\"success\":false,\"message\":\"请先发送验证码\"}";
        }

        if (!savedCode.equals(inputCode)) {
            return "{\"success\":false,\"message\":\"验证码不正确\"}";
        }

        System.out.println("邮箱 " + savedEmail + " 验证通过，学校：" + savedSchool);

        return "{\"success\":true,\"message\":\"验证成功\"}";
    }
    
    @PostMapping("/get-school")
    @ResponseBody
    public Map<String, Object> getSchool(HttpSession session) {
        String savedSchool = (String) session.getAttribute("emailSchool");

        Map<String, Object> res = new HashMap<>();
        if (savedSchool == null) {
            res.put("success", false);
            res.put("message", "没有找到学校信息，请重新验证");
            return res;
        }

        System.out.println("获取学校信息：" + savedSchool);
        // 清理session验证码相关数据
        session.removeAttribute("emailCode");
        session.removeAttribute("emailAddress");
        session.removeAttribute("emailSchool");

        res.put("success", true);
        res.put("school", savedSchool);
        return res;
    }
    

}
