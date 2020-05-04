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
 * 读库数据源配置
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.cent.demo.dydatasource.mapper.read",
        sqlSessionTemplateRef = "readableSqlSessionTemplate")
public class ReadableDBConfig {

    /**
     * 环境变量json配置
     * readableDS={"driver":"com.mysql.cj.jdbc.Driver", "url":"jdbc:mysql://localhost:3306/mytest?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "username":"root", "password":"123456"}
     */
    @Value("${readableDS}")
    private String readableJdbcString;

    @Bean("readableDataSource")
    public DataSource readableDataSource() {
        log.debug("readableJdbcString: {}", readableJdbcString);

        // jdbc配置属性获取方法二：每项属性组成jsonString格式一起配置，获取配置jsonString再解析使用
        JdbcConfig readableJdbcConfig = JSON.parseObject(readableJdbcString, JdbcConfig.class);
        log.debug("readableJdbcConfig: {}", readableJdbcConfig);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName("HikariPool-Readable");
        hikariDataSource.setDriverClassName(readableJdbcConfig.getDriver());
        hikariDataSource.setJdbcUrl(readableJdbcConfig.getUrl());
        hikariDataSource.setUsername(readableJdbcConfig.getUsername());
        hikariDataSource.setPassword(readableJdbcConfig.getPassword());
        return hikariDataSource;
    }

    @Bean(name = "readableTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("readableDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "readableSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("readableDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/read/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "readableSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("readableSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
