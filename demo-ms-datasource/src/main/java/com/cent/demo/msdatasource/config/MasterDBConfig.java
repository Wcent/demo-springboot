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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 主库数据源配置
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.cent.demo.msdatasource.mapper.master",
        sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterDBConfig {

    /**
     * 环境变量json配置
     * masterDS={"driver":"com.mysql.cj.jdbc.Driver", "url":"jdbc:mysql://localhost:3306/mydata?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai", "username":"root", "password":"123456"}
     */
    @Value("${masterDS:{\"driver\":\"com.mysql.cj.jdbc.Driver\", \"url\":\"jdbc:mysql://localhost:3306/mydata?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai\", \"username\":\"root\", \"password\":\"123456\"}}")
    private String masterJdbcString;

    @Primary
    @Bean("masterDataSource")
    public DataSource masterDataSource() {
        log.debug("masterJdbcString: {}", masterJdbcString);

        // jdbc配置属性获取方法二：每项属性组成jsonString格式一起配置，获取配置jsonString再解析使用
        JdbcConfig masterJdbcConfig = JSON.parseObject(masterJdbcString, JdbcConfig.class);
        log.debug("masterJdbcConfig: {}", masterJdbcConfig);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName("HikariPool-Master");
        hikariDataSource.setDriverClassName(masterJdbcConfig.getDriver());
        hikariDataSource.setJdbcUrl(masterJdbcConfig.getUrl());
        hikariDataSource.setUsername(masterJdbcConfig.getUsername());
        hikariDataSource.setPassword(masterJdbcConfig.getPassword());
        return hikariDataSource;
    }

    @Primary
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "masterSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/master/*.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "masterSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
