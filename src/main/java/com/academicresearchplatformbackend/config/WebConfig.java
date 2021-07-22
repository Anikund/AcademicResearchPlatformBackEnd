package com.academicresearchplatformbackend.config;

import com.academicresearchplatformbackend.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //修改allowedOrigins为前端端口
        registry.addMapping("/**").allowCredentials(true)
                .allowedOrigins("http://localhost:9527")
                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS")
                .allowedHeaders("*");
    }
}
