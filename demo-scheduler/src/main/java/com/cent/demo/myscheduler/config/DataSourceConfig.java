package com.cent.demo.myscheduler.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 自定义数据源配置
 *
 * @author cent 2020/3/31
 */
@Configuration
@Slf4j
@MapperScan(basePackages = "com.cent.demo.myscheduler.mapper",
        sqlSessionTemplateRef = "mySqlSessionTemplate")
public class DataSourceConfig {

    @Autowired
    private JdbcConfig jdbcConfig;

    @Bean("myDataSource")
    DataSource dataSource() {

        HikariDataSource ds = new HikariDataSource();
        ds.setPoolName("HikariPool-My");
        ds.setDriverClassName(jdbcConfig.getDriver());
        ds.setJdbcUrl(jdbcConfig.getUrl());
        ds.setUsername(jdbcConfig.getUsername());
        ds.setPassword(jdbcConfig.getPassword());

        return ds;
    }

    @Bean(name = "myTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("myDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("myDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/**/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "mySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("mySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
