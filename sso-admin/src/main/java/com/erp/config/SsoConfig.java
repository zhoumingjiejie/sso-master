package com.erp.config;

import com.erp.filter.SsoWebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 0.0.0
 * @ProjectName: [sso-master]
 * @Package: [com.erp.config.SsoConfig]
 * @Description 单点登录配置
 * @Date 2020/3/25 10:15
 */
@Configuration
public class SsoConfig {

    /**
     * 定义ssoWebFilter过滤器
     */
    @Bean
    public FilterRegistrationBean<SsoWebFilter> filterRegistrationBean() {
        FilterRegistrationBean<SsoWebFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setName("ssoWebFilter");
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setFilter(new SsoWebFilter());
        return filterRegistrationBean;
    }
}
