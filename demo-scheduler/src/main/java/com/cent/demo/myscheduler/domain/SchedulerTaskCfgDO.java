package com.cent.demo.myscheduler.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 定时任务配置
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
@Data
@Builder
public class SchedulerTaskCfgDO {
    private long id;
    private String name;
    private String cron;
    private int status;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private long takeTime;
    private int version;
    private LocalDateTime mntTime;
    private LocalDateTime addTime;
}
