package com.intuit.demo.configuration;

import com.intuit.demo.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    @Autowired
    private ApiKeyRequestFilter apiKeyRequestFilter;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private RequestFilter requestFilter;

    @Autowired
    private DriverAuthorizationFilter driverAuthorizationFilter;

    @Autowired
    private AdminAuthorizationFilter adminAuthorizationFilter;

    @Autowired
    private BaseOncePerRequestFilter baseOncePerRequestFilter;

    @Bean
    public FilterRegistrationBean<BaseOncePerRequestFilter> baseOncePerRequestFilterRegistrationBean() {
        FilterRegistrationBean<BaseOncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(baseOncePerRequestFilter);
        registration.addUrlPatterns("*");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<RequestFilter> requestFilterRegistrationBean() {
        FilterRegistrationBean<RequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(requestFilter);
        registration.addUrlPatterns("*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ApiKeyRequestFilter> apiKeyRequestFilterRegistrationBean() {
        FilterRegistrationBean<ApiKeyRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(apiKeyRequestFilter);
        registration.addUrlPatterns("*");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistrationBean() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authenticationFilter);
        registration.addUrlPatterns("*");
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<DriverAuthorizationFilter> driverAuthorisationFilterRegistrationBean() {
        FilterRegistrationBean<DriverAuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(driverAuthorizationFilter);
        registration.addUrlPatterns("/v1/driver/*");
        registration.setOrder(4);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AdminAuthorizationFilter> adminAuthorisationFilterRegistrationBean() {
        FilterRegistrationBean<AdminAuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(adminAuthorizationFilter);
        registration.addUrlPatterns("/v1/admin/*");
        registration.setOrder(4);
        return registration;
    }
}
