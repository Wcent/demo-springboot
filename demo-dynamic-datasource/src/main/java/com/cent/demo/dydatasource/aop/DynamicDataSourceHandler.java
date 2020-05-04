package com.cent.demo.dydatasource.aop;

import com.cent.demo.dydatasource.annotation.UseDataSource;
import com.cent.demo.dydatasource.config.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 动态数据源选择处理强
 * 使用前自动设置，使用后自动清除
 *
 * @author Vincent
 * @version 1.0 2020/5/4
 */
@Slf4j
@Component
@Aspect
// Spring 事务管理(DataSourceTransactionManager)只支持单库事务，
// 开启事务，会将数据源缓存到 DataSourceTransactionObject 对象中进行后续的 commit 或 rollback 等操作。
// 即开启了事务后是不能切换数据源的，切换数据源会无效，因此切换数据源要在开启事务之前执行。
@Order(-1) // 保证事务开启前先切换数据源
public class DynamicDataSourceHandler {

    /**
     * 拦截UseDataSource数据源选择器注解
     * 对@within作用于标注目标类下所有方法，但注意标注接口类无效
     * 对@annotation作用于标注的指定目标类方法
     */
    @Pointcut("@within(com.cent.demo.dydatasource.annotation.UseDataSource) ||" +
            "@annotation(com.cent.demo.dydatasource.annotation.UseDataSource)")
    public void pointcut() {

    }

    /**
     * 处理前设置数据源
     * @param joinPoint 切点
     * @param useDataSource 注解参数
     */
    @Before("pointcut() && @annotation(useDataSource)")
    public void setDataSource(JoinPoint joinPoint, UseDataSource useDataSource) {
        // 获取注解指定使用数据源类型
        DynamicDataSource.DataSourceType type = useDataSource.type();
        DynamicDataSource.setDataSource(type);
        log.info("设置数据源：类：{}，方法：{}，数据源：{}",
                joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(),
                type);
    }

    /**
     * 处理后清空数据源
     * @param joinPoint 切点
     * @param useDataSource 注解参数
     */
    @After("pointcut() && @annotation(useDataSource)")
    public void removeDataSource(JoinPoint joinPoint, UseDataSource useDataSource) {
        // 清空当前线程上下文数据源配置
        DynamicDataSource.clear();
        log.info("清除数据源：类：{}，方法：{}，数据源：{}",
                joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(),
                useDataSource.type());
    }
}
