package com.cent.demo.datasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @class: DataSourceConfig
 * @description: 自定义数据源配置
 * @author: cent
 * @create: 2020/3/31
 **/
@Configuration
public class DataSourceConfig {

    @Bean("myDataSource")
    DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/mytest?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }
}
