package com.cent.demo.myscheduler.scheduler;

import lombok.extern.slf4j.Slf4j;
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
public class SimpleScheduler {

    /**
     * 固定频率执行任务
     * 每5秒开启一个任务，任务时长10秒钟
     */
    @Scheduled(fixedRate = 5*1000)
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
