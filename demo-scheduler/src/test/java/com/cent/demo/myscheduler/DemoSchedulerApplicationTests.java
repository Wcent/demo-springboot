package com.cent.demo.myscheduler;

import com.cent.demo.myscheduler.domain.SchedulerTaskCfgDO;
import com.cent.demo.myscheduler.mapper.SchedulerTaskCfgDAO;
import com.cent.demo.myscheduler.service.SimpleAsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Slf4j
@SpringBootTest
class DemoSchedulerApplicationTests {

	@Autowired
	private SimpleAsyncTask simpleAsyncTask;

	@Autowired
	private SchedulerTaskCfgDAO schedulerTaskCfgDAO;

	@Test
	void contextLoads() {
	}

	@Test
	void runAsyncTask() {
		simpleAsyncTask.runAsync();
		simpleAsyncTask.runAsyncException();
	}

	@Test
	void schedulerTaskCfgDAO() {

		final String name = String.format("DynamicSchedulerTask(%s)", new Random().nextInt(100));
		final String cron = "0/5 * * * * *";
		final int status = 0;
		final LocalDateTime time = LocalDateTime.parse("0001-01-01T00:00:00");
		SchedulerTaskCfgDO schedulerTaskCfgDO = SchedulerTaskCfgDO.builder().
				name(name)
				.cron(cron)
				.status(status)
				.beginTime(time)
				.endTime(time)
				.mntTime(LocalDateTime.now())
				.addTime(LocalDateTime.now())
				.build();
		log.info("初始化定时任务配置：{}", schedulerTaskCfgDO);

		int cnt = schedulerTaskCfgDAO.insertSchedulerTaskCfg(schedulerTaskCfgDO);
		Assertions.assertEquals(1, cnt);

		SchedulerTaskCfgDO schedulerTaskCfgQuery = schedulerTaskCfgDAO.getSchedulerTaskCfg(name);
		Assertions.assertNotNull(schedulerTaskCfgQuery);
		Assertions.assertEquals(name, schedulerTaskCfgQuery.getName());
		Assertions.assertEquals(cron, schedulerTaskCfgQuery.getCron());
		Assertions.assertEquals(status, schedulerTaskCfgQuery.getStatus());
		log.info("新增定时任务配置：{}", schedulerTaskCfgQuery);

		LocalDateTime begin = LocalDateTime.now(ZoneId.systemDefault());
		try {
			Thread.sleep((new Random().nextInt(10)+1)*1000);
		} catch (InterruptedException e) {
			log.error("休眠中断", e);
		}
		LocalDateTime end = LocalDateTime.now();
		schedulerTaskCfgDO.setBeginTime(begin);
		schedulerTaskCfgDO.setEndTime(end);
		schedulerTaskCfgDO.setMntTime(LocalDateTime.now());
		schedulerTaskCfgDO.setTakeTime(ChronoUnit.MILLIS.between(begin, end));
		cnt = schedulerTaskCfgDAO.updateSchedulerTaskCfgRunInfo(schedulerTaskCfgDO);
		Assertions.assertEquals(1, cnt);
		schedulerTaskCfgQuery = schedulerTaskCfgDAO.getSchedulerTaskCfg(name);
		Assertions.assertNotNull(schedulerTaskCfgQuery);
		log.info("更新定时任务配置：{}", schedulerTaskCfgQuery);

		cnt = schedulerTaskCfgDAO.deleteSchedulerTaskCfg(schedulerTaskCfgQuery);
		Assertions.assertEquals(1, cnt);
		log.info("删除定时任务配置");

		schedulerTaskCfgQuery = schedulerTaskCfgDAO.getSchedulerTaskCfg(name);
		Assertions.assertNull(schedulerTaskCfgQuery);
	}
}
