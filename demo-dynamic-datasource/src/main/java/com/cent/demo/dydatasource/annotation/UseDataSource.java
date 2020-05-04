package com.cent.demo.dydatasource.annotation;

import com.cent.demo.dydatasource.config.DynamicDataSource;

import java.lang.annotation.*;

/**
 * 动态选择使用数据源注解
 * 注解于类，影响所有方法
 * 注解于方法，仅作用于当前方法
 *
 * @author Vincent
 * @version 1.0 2020/5/4
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseDataSource {

    /**
     * 指定使用数据源类型，默认写库数据源
     * @return 数据源类型
     */
    DynamicDataSource.DataSourceType type() default DynamicDataSource.DataSourceType.Writable;
}
