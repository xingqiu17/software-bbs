package com.quark.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;


@SpringBootApplication
@EnableCaching//缓存支持
public class RestApplication {

    public static void main(String[] args) throws IOException {
        // 加载 rest.properties
        Properties properties = new Properties();
        InputStream in = RestApplication.class.getClassLoader().getResourceAsStream("rest.properties");
        properties.load(in);

        SpringApplication app = new SpringApplication(RestApplication.class);
        app.setDefaultProperties(properties);

        // 运行并取得上下文
        ConfigurableApplicationContext ctx = app.run(args);

        // 从上下文里拿 Environment，然后打印
        Environment env = ctx.getEnvironment();
        String redisPass = env.getProperty("spring.redis.password");
        String[] profiles = env.getActiveProfiles();
        System.out.println(">>> [Env Check] spring.redis.password = [" + redisPass + "]");
        System.out.println(">>> [Env Check] active profiles = " + Arrays.toString(profiles));
    }


}

