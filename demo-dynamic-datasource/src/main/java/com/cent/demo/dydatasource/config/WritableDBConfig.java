package com.cent.demo.dydatasource.config;

import com.alibaba.fastjson.JSON;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 写库数据源配置
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.cent.demo.dydatasource.mapper.write",
        sqlSessionTemplateRef = "writableSqlSessionTemplate")
public class WritableDBConfig {

    /**
     * 环境变量json配置
     * writableDS={"driver":"com.mysql.cj.jdbc.Driver", "url":"jdbc:mysql://localhost:3306/mydata?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai", "username":"root", "password":"123456"}
     */
    @Value("${writableDS:{\"driver\":\"com.mysql.cj.jdbc.Driver\", \"url\":\"jdbc:mysql://localhost:3306/mydata?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\", \"username\":\"root\", \"password\":\"123456\"}}")
    private String writableJdbcString;

    @Bean("writableDataSource")
    public DataSource writableDataSource() {
        log.debug("writableJdbcString: {}", writableJdbcString);

        // jdbc配置属性获取：每项属性组成jsonString格式一起配置，获取配置jsonString再解析使用
        JdbcConfig writableJabConfig = JSON.parseObject(writableJdbcString, JdbcConfig.class);
        log.debug("writableJdbcConfig: {}", writableJabConfig);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName("HikariPool-Writable");
        hikariDataSource.setDriverClassName(writableJabConfig.getDriver());
        hikariDataSource.setJdbcUrl(writableJabConfig.getUrl());
        hikariDataSource.setUsername(writableJabConfig.getUsername());
        hikariDataSource.setPassword(writableJabConfig.getPassword());
        return hikariDataSource;
    }

    @Bean(name = "writableTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("writableDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "writableSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("writableDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/write/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "writableSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("writableSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
