package com.cent.demo.dydatasource.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置
 * @author cent 2020/3/31
 */
@Configuration
@Slf4j
public class DynamicDBConfig {

    @Primary
    @DependsOn({"writableDataSource", "readableDataSource"}) // 显式指定依赖，避免循环引用
    @Bean("dynamicDataSource")
    public DataSource dataSource(@Qualifier("writableDataSource") DataSource writableDataSource,
                                 @Qualifier("readableDataSource") DataSource readableDataSource) {

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 设置可用数据源，配置读库/写库
        Map<Object, Object> targetDataSource = new HashMap<>(2);
        targetDataSource.put(DynamicDataSource.DataSourceType.Writable, writableDataSource);
        targetDataSource.put(DynamicDataSource.DataSourceType.Readable, readableDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSource);
        // 设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(writableDataSource);
        // 配置完成
        dynamicDataSource.afterPropertiesSet();

        return dynamicDataSource;
    }

    @Primary
    @Bean(name = "dynamicTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("dynamicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "dynamicSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/**/*.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "dynamicSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("dynamicSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
