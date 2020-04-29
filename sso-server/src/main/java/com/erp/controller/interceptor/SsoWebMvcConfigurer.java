package com.erp.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.config.SsoWebMvcConfigurer]
 * @Description web mvc config
 * @Date 2020/3/24 9:57
 */
@Slf4j
@Configuration
public class SsoWebMvcConfigurer implements WebMvcConfigurer {
    /**
     * 重写添加拦截器方法并添加配置拦截器
     *
     * @param registry 注册拦截
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加权限拦截
        registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**");
    }

}
