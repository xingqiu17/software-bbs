package com.quark.porent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter; // ← 继承适配器

@SuppressWarnings("deprecation") // 旧版本专用
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${UPLOAD_PATH:D:/home/images/upload/}")
    private String uploadPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/upload/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
