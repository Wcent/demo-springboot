package com.cent.demo.datasource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器配置，统一设置http响应头部参数
 *
 * @author Vincent
 * @version 1.0 2020/8/8
 */
@Configuration
public class FilterConfig extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        httpServletResponse.setHeader("Connection", "close");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
