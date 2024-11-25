package com.example.handle.function;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptors())
                .addPathPatterns("/change/updateImageInfo","/change/deleteImage","/post/upload","userinfo")  // 其他接口token验证
                .excludePathPatterns("/getEngineeringTeamName","/getStratumName","/post/login","/post/register",
                        "/change/showImageInfo","/get/getImageInfo","/uploadimage","/change/updateSubmit");  // 所有用户都放行
    }
}
