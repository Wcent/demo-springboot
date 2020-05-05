package com.cent.demo.myscheduler.config;

import com.cent.demo.myscheduler.domain.SchedulerTaskCfgDO;
import com.cent.demo.myscheduler.mapper.SchedulerTaskCfgDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务调度器线程池配置
 * 实现接口SchedulingConfigurer#configureTasks方法完成定时任务线程池配置
 * 注意：ThreadPoolTaskScheduler任务队列无限制，可能会导致OOM，一般推荐使用ThreadPoolTaskExecutor
 *      ThreadPoolTaskExecutor实现TaskScheduler接口，以支持定时任务（不需要引入额外的定时任务框架，如Quartz）
 *
 * @author Vincent
 * @version 1.0 2020/5/5
 */
@Slf4j
@Configuration
@EnableScheduling
public class ThreadPoolSchedulerConfig implements SchedulingConfigurer {

    /**
     * 定时任务条件表达式
     * spring的cron表达式由second, minute, hour, day of month, month, day(s) of week空格分隔的6个域组成
     */
    private String cron = "*/5 * * * * *";

    private final Random random = new Random();

    private final String name = "DynamicSchedulerTask";

    @Autowired
    private SchedulerTaskCfgDAO schedulerTaskCfgDAO;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        // 初始化任务线程池，注意ThreadPoolTaskScheduler任务队列无限制，可能会导致OOM
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setBeanName("MyThreadPoolTaskScheduler");
        threadPoolTaskScheduler.setThreadNamePrefix("MyThreadPoolTaskScheduler-");
        threadPoolTaskScheduler.setPoolSize(4);
        // 拒绝策略：终止任务
        threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 任务执行异常处理强
        threadPoolTaskScheduler.setErrorHandler(throwable -> log.error("定时任务执行异常", throwable));
        // 配置生效
        threadPoolTaskScheduler.initialize();

        // 任务调度器配置线程池
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

        // 配置可动态调整触发条件的定时任务
        scheduledTaskRegistrar.addTriggerTask(() -> {
                    String newCron = String.format("*/%s * * * * *", random.nextInt(10)+1);
                    log.warn("{}触发定时任务数{}，触发条件{} => {}，",
                            LocalDateTime.now(),
                            scheduledTaskRegistrar.getTriggerTaskList().size(),
                            cron,
                            newCron);
                    log.warn("触发定时任务：{}", scheduledTaskRegistrar.getTriggerTaskList());
                    log.warn("Tregger:{}, Cron:{}, FixedDelay:{}, FixedRate:{}",
                            scheduledTaskRegistrar.getTriggerTaskList().size(),
                            scheduledTaskRegistrar.getCronTaskList().size(),
                            scheduledTaskRegistrar.getFixedDelayTaskList().size(),
                            scheduledTaskRegistrar.getFixedRateTaskList().size());
                    cron = newCron;
                },
                triggerContext -> {
                    Date nextExecutionTime = new CronTrigger(cron).nextExecutionTime(triggerContext);
                    log.warn("动态调整触发条件{}，下次任务执行时间：{}", cron, nextExecutionTime);
                    return nextExecutionTime;
                });

        // 配置可动态调整数据库表触发条件的定时任务
        scheduledTaskRegistrar.addTriggerTask(() -> {
                    LocalDateTime begin = LocalDateTime.now();
                    SchedulerTaskCfgDO schedulerTaskCfg = schedulerTaskCfgDAO.getSchedulerTaskCfg(name);
                    LocalDateTime end = LocalDateTime.now();
                    schedulerTaskCfg.setBeginTime(begin);
                    schedulerTaskCfg.setEndTime(end);
                    schedulerTaskCfg.setTakeTime(ChronoUnit.MILLIS.between(begin, end));
                    if (schedulerTaskCfgDAO.updateSchedulerTaskCfgRunInfo(schedulerTaskCfg) > 0) {
                        schedulerTaskCfg = schedulerTaskCfgDAO.getSchedulerTaskCfg(name);
                        log.info("{}更新定时任务执行信息成功：{}，", begin, schedulerTaskCfg);
                    } else {
                        log.warn("{}更新定时任务执行信息失败：{}，", begin, schedulerTaskCfg);
                    }
                },
                triggerContext -> {
                    String newCron;
                    SchedulerTaskCfgDO schedulerTaskCfgDO = schedulerTaskCfgDAO.getSchedulerTaskCfg(name);
                    if (schedulerTaskCfgDO == null || StringUtils.isEmpty(schedulerTaskCfgDO.getCron())) {
                        newCron = cron;
                    } else {
                        newCron = schedulerTaskCfgDO.getCron();
                    }
                    Date nextExecutionTime = new CronTrigger(newCron).nextExecutionTime(triggerContext);
                    log.warn("动态调整数据库表定时任务配置触发条件{}，下次任务执行时间：{}", newCron, nextExecutionTime);
                    return nextExecutionTime;
                });
    }
}
