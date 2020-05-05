package com.cent.demo.myscheduler.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 简单的定时任务调度器
 * 注解直接定义任务触发条件，自动配置实现SchedulingConfigurer接口的任务线程池
 * 注意：默认使用的线程池只有一个工作线程，配置的多个任务（包括不同类）均在同一线程中执行，并且会阻塞相互影响
 *
 * @author Vincent
 * @version 1.0 2020/5/5
 */
@Slf4j
@Service
public class SimpleExceptionScheduler {

    /**
     * 模拟任务执行异常
     */
    @Scheduled(fixedDelay = 5*60*1000)
    public void runException() {
        log.warn("模拟定时任务执行异常终止：{}{}", Thread.currentThread(), LocalDateTime.now());
        throw new RuntimeException("模拟定时任务执行异常");
    }

    /**
     * 模拟异步定时任务执行异常
     */
    @Scheduled(fixedDelay = 5*60*1000)
    @Async
    public void runAsyncException() {
        log.warn("模拟异步定时任务执行异常终止：{}{}", Thread.currentThread(), LocalDateTime.now());
        throw new RuntimeException("模拟异步定时任务执行异常");
    }
}
