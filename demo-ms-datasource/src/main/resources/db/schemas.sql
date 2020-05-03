CREATE TABLE `sys_ctl` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置序号',
  `name` char(20) NOT NULL DEFAULT '""' COMMENT '配置名称',
  `ctl_value` varchar(200) NOT NULL DEFAULT '""' COMMENT '配置值',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '配置状态，0：生效，1：失效',
  `version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  `mnt_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '维护时间',
  `add_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表'