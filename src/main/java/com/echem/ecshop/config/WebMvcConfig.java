package com.echem.ecshop.config;

import com.echem.ecshop.interceptor.VisitCounterInterceptor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final VisitCounterInterceptor visitCounterInterceptor;
    
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(visitCounterInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", 
                        "/js/**", 
                        "/images/**",
                        "/static/**",
                        "/favicon.ico"
                );
    }
}
