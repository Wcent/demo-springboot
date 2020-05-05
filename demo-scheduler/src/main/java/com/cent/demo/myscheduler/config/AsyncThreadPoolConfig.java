package com.cent.demo.myscheduler.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池配置
 * 实现接口AsyncConfigurer#getAsyncExecutor方法完成定时任务线程池配置
 * 实现接口AsyncConfigurer#getAsyncUncaughtExceptionHandler方法捕获任务执行异常处理
 * 注意：ThreadPoolTaskScheduler任务队列无限制，可能会导致OOM，推荐使用ThreadPoolTaskExecutor
 *      ThreadPoolTaskExecutor实现TaskScheduler接口，以支持定时任务（不需要引入额外的定时任务框架，如Quartz）
 *
 * @author Vincent
 * @version 1.0 2020/5/5
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncThreadPoolConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {

        // 初始化任务线程池，注意ThreadPoolTaskScheduler任务队列无限制，可能会导致OOM
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setBeanName("MyThreadPoolTaskExecutor");
        threadPoolTaskExecutor.setThreadNamePrefix("MyThreadPoolTaskExecutor-");
        // 任务队列未满时，核心工作线程数
        threadPoolTaskExecutor.setCorePoolSize(4);
        // 任务队列满后，工作线程数扩增上限
        threadPoolTaskExecutor.setMaxPoolSize(10);
        // 设置任务队列大小，避免任务处理不过来，无限增长导致OOM
        threadPoolTaskExecutor.setQueueCapacity(100);
        // 拒绝策略：终止任务
        // 若是搭配定时任务使用，则抛出的TaskRejectedException异常
        // 优先会被定时任务调度器ThreadPoolTaskScheduler#setErrorHandlersetErrorHandler设置的异常处理强捕获
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 配置生效
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("异步任务处理异常", ex);
    }
}
