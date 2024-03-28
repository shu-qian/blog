package com.gardenia.blog.config;

import com.gardenia.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/9 - 22:06
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
        //本地测试 端口不一致 也算跨域

        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
        //域名就是IP地址+端口
        //允许8080访问我们所有接口
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //假设拦截test接口后续遇到实际需要拦截的接口时，再配置真正的拦截接口
        //.addPathPatterns("/**").excludePathPatterns("/login").addPathPatterns("/register");
        //拦截所有路径，除了登录和注册，常用的一种拦截方式，这里不需要，先测试
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}
