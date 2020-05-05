package com.cent.demo.myscheduler.mapper;


import com.cent.demo.myscheduler.domain.SchedulerTaskCfgDO;

/**
 * 定时任务配置DAO接口
 *
 * @author Vincent
 * @version 1.0 2020/5/3
 */
public interface SchedulerTaskCfgDAO {

    /**
     * 新增定时任务配置
     * @param schedulerTaskCfgDO 定时任务配置领域实体类对象
     * @return 影响记录数
     */
    int insertSchedulerTaskCfg(SchedulerTaskCfgDO schedulerTaskCfgDO);

    /**
     * 更新定时任务配置执行信息
     * @param schedulerTaskCfgDO 定时任务配置领域实体类对象
     * @return 影响记录数
     */
    int updateSchedulerTaskCfgRunInfo(SchedulerTaskCfgDO schedulerTaskCfgDO);

    /**
     * 删除定时任务配置
     * @param schedulerTaskCfgDO 定时任务配置领域实体类对象
     * @return 影响记录数
     */
    int deleteSchedulerTaskCfg(SchedulerTaskCfgDO schedulerTaskCfgDO);

    /**
     * 查询定时任务配置
     * @param name 任务名称
     * @return 定时任务配置
     */
    SchedulerTaskCfgDO getSchedulerTaskCfg(String name);
}
