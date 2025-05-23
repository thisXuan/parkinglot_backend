package com.parkinglot_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //配置类
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .excludePathPatterns(
                        "/user/**",
                        "/store/**",
                        "/navigation/**",
                        "/voucher/getVoucherByShopId",
                        "/shopLocation/**",
                        "/parking/nulllocation",
                        "/parking/fulllocation"
                ).excludePathPatterns(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**"
                );

    }
}
