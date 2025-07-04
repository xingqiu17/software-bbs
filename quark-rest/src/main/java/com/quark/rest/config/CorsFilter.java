package com.quark.rest.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 跨域的支持（支持带凭证）
 */
@Component
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 1、改成你前端的地址，不能再用 "*"
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8082");
        // 2、允许带上凭证（cookie）
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 3、允许的方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        // 4、允许的头（如果你还会传 Content-Type、Authorization 等，也要加上）
        response.setHeader("Access-Control-Allow-Headers",
            "X-Requested-With, Content-Type, Accept, Origin, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");

        // 对 preflight 请求快速返回
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() { }
}
