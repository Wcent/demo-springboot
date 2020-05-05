package com.cent.demo.myscheduler.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 简单的异步定时任务调度器
 * 注解直接定义任务触发条件，注解指定异步任务
 * 注意：注解@Async未显示指定线程池时使用默认的线程池SimpleAsyncTaskExecutor(不重用线程，每次都得新建工作线程)，
 *      可实现AsyncConfigurer接口配置类，会自动配置实现类getAsyncExecutor方法返回的异步任务线程池
 *
 * @author Vincent
 * @version 1.0 2020/5/5
 */
@Slf4j
@Service
public class SimpleAsyncScheduler {

    /**
     * 固定频率执行任务
     * 每5秒开启一个任务，任务时长10秒钟
     */
    @Scheduled(fixedRate = 5*1000)
    @Async
    public void runPerMinute() {
        log.info("固定间隔任务：线程{}{}开始执行定时任务：固定频率每5秒跑一次，线程休眠模拟任务时长10秒钟",
                Thread.currentThread(), LocalDateTime.now());
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            log.error("休眠中断", e);
        }
        log.info("固定间隔任务：线程{}{}结束执行定时任务：固定频率每5秒",
                Thread.currentThread(), LocalDateTime.now());
    }

    /**
     * 固定间隔执行任务
     * 每间隔5秒开启一个任务，任务时长10秒钟
     */
    @Scheduled(fixedDelay = 5*1000)
    @Async
    public void runAfterOneMinute() {
        log.info("固定频率任务：线程{}{}开始执行定时任务：固定间隔5秒跑一次，线程休眠模拟任务时长10秒钟",
                Thread.currentThread(), LocalDateTime.now());
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            log.error("休眠中断", e);
        }
        log.info("固定频率任务：线程{}{}结束执行定时任务：固定间隔5秒",
                Thread.currentThread(), LocalDateTime.now());
    }
}
