CREATE TABLE `scheduler_task_cfg` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '任务编号',
  `name` varchar(50) NOT NULL COMMENT '任务名称',
  `cron` varchar(20) NOT NULL COMMENT '触发条件，spring cron表达式',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '任务状态，0：生效，1：暂停，2：失效',
  `begin_time` datetime NOT NULL COMMENT '上次任务开始时间',
  `end_time` datetime NOT NULL COMMENT '上次任务结束时间',
  `take_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次任务耗时，单位毫秒',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `mnt_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '维护时间',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务配置表'