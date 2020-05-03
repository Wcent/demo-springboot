package com.cent.demo.msdatasource.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @class: JdbcConfig
 * @description: 自定义数据源配置
 * @author: cent
 * @create: 2020/4/25
 **/
@Configuration
@Data
public class JdbcConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
}
