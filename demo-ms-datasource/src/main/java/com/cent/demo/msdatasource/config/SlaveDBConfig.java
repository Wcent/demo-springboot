package com.cent.demo.msdatasource.config;

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
 * 从库数据源配置
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.cent.demo.msdatasource.mapper.slave",
        sqlSessionTemplateRef = "slaveSqlSessionTemplate")
public class SlaveDBConfig {

    /**
     * 环境变量json配置
     * slaveDS={"driver":"com.mysql.cj.jdbc.Driver", "url":"jdbc:mysql://localhost:3306/mytest?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "username":"root", "password":"123456"}
     */
    @Value("${slaveDS}")
    private String slaveJdbcString;

    @Bean("slaveDataSource")
    public DataSource slaveDataSource() {
        log.debug("slaveJdbcString: {}", slaveJdbcString);

        // jdbc配置属性获取方法二：每项属性组成jsonString格式一起配置，获取配置jsonString再解析使用
        JdbcConfig slaveJdbcConfig = JSON.parseObject(slaveJdbcString, JdbcConfig.class);
        log.debug("slaveJdbcConfig: {}", slaveJdbcConfig);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName("HikariPool-Slave");
        hikariDataSource.setDriverClassName(slaveJdbcConfig.getDriver());
        hikariDataSource.setJdbcUrl(slaveJdbcConfig.getUrl());
        hikariDataSource.setUsername(slaveJdbcConfig.getUsername());
        hikariDataSource.setPassword(slaveJdbcConfig.getPassword());
        return hikariDataSource;
    }

    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("slaveDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("slaveDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/slave/*.xml"));
        return factoryBean.getObject();
    }

    @Bean(name = "slaveSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("slaveSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
