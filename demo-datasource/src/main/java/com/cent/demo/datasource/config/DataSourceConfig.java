package com.cent.demo.datasource.config;

import com.alibaba.fastjson.JSON;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @class: DataSourceConfig
 * @description: 自定义数据源配置
 * @author: cent
 * @create: 2020/3/31
 **/
@Configuration
@Slf4j
public class DataSourceConfig {

    @Autowired
    private Environment environment;

    @Value("${mydatasource}")
    private String myDataSourceJsonString;

    @Autowired
    private JdbcConfig jdbcConfig;

    @Bean("myDataSource")
    DataSource dataSource() {
        // 获取环境变量配置方法一：先获取环境变量，再手工获取配置项
        log.debug(environment.getProperty("mydatasource"));
        // 获取环境变量配置方法二：自动配置
        log.debug("mydatasource: {}", myDataSourceJsonString);

        // jdbc配置属性获取方法一：每项属性在环境变量中独立单独配置
        log.debug("jdbcConfig: {}", jdbcConfig);

        // jdbc配置属性获取方法二：每项属性组成jsonString格式一起配置，获取配置jsonString再解析使用
        JdbcConfig myJdbcConfig = JSON.parseObject(myDataSourceJsonString, JdbcConfig.class);
        log.debug("myJdbcConfig: {}", myJdbcConfig);

        HikariDataSource ds = new HikariDataSource();
//        ds.setDriverClassName(jdbcConfig.getDriver());
//        ds.setJdbcUrl(jdbcConfig.getUrl());
//        ds.setUsername(jdbcConfig.getUsername());
//        ds.setPassword(jdbcConfig.getPassword());

        ds.setDriverClassName(myJdbcConfig.getDriver());
        ds.setJdbcUrl(myJdbcConfig.getUrl());
        ds.setUsername(myJdbcConfig.getUsername());
        ds.setPassword(myJdbcConfig.getPassword());

        return ds;
    }
}
