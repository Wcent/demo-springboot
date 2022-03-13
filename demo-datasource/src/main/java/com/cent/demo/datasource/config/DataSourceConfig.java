package com.cent.demo.datasource.config;

import com.alibaba.fastjson.JSON;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 自定义数据源配置
 *
 * @author cent 2020/3/31
 */
@Configuration
@Slf4j
public class DataSourceConfig {

    @Autowired
    private Environment environment;

    /**
     * 环境变量json配置
     * mydatasource={"driver":"com.mysql.cj.jdbc.Driver", "url":"jdbc:mysql://localhost:3306/mydata?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai", "username":"root", "password":"123456"}
     */
    @Value("${mydatasource:{\"driver\":\"com.mysql.cj.jdbc.Driver\", \"url\":\"jdbc:mysql://localhost:3306/mydata?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\", \"username\":\"root\", \"password\":\"123456\"}}")
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
//        ds.setPoolName("HikariPool-My");
//        ds.setDriverClassName(jdbcConfig.getDriver());
//        ds.setJdbcUrl(jdbcConfig.getUrl());
//        ds.setUsername(jdbcConfig.getUsername());
//        ds.setPassword(jdbcConfig.getPassword());

        ds.setPoolName("HikariPool-My");
        ds.setDriverClassName(myJdbcConfig.getDriver());
        ds.setJdbcUrl(myJdbcConfig.getUrl());
        ds.setUsername(myJdbcConfig.getUsername());
        ds.setPassword(myJdbcConfig.getPassword());

        return ds;
    }

    @Bean(name = "myTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("myDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
