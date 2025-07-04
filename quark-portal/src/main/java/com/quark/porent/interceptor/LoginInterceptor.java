package com.quark.porent.interceptor;

import com.quark.porent.entity.User;
import com.quark.porent.service.UserService;
import com.quark.porent.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;     

/**
 * @Author LHR
 * Create By 2017/8/27
 *
 * 登录拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Value("${cookie_name}")
    private String CookieName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 打印所有 Cookies
        if (request.getCookies() != null) {
            System.out.println(">> Incoming Cookies:");
            for (Cookie c : request.getCookies()) {
                System.out.println("   " + c.getName() + " = " + c.getValue());
            }
        } else {
            System.out.println(">> No Cookies on request");
        }

        // 2. 从 CookieUtils 里取 token
        String token = CookieUtils.getCookieValue(request, "QUARK_TOKEN");
        System.out.println(">> Token from CookieUtils = [" + token + "]");

        // 3. 如果 token 为空
        if (token == null) {
            System.out.println(">> No token: redirecting to login");
            response.sendRedirect("/user/login");
            return false;
        }

        // 4. 调用 UserService 验证
        User user = userService.getUserByApi(token);
        System.out.println(">> userService.getUserByApi returned = " + user);

        // 5. 如果 user 为 null
        if (user == null) {
            System.out.println(">> user is null: redirecting to login");
            response.sendRedirect("/user/login");
            return false;
        }

        // 6. 成功，继续执行
        System.out.println(">> User is valid, allowing request");
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
