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

        registry.addMapping("/**").allowCredentials(true)
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS")
                .allowedHeaders("*");
    }
}
