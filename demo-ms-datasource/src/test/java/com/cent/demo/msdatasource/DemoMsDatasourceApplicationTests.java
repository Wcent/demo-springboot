package com.cent.demo.msdatasource;

import com.cent.demo.msdatasource.domain.SysCtlDO;
import com.cent.demo.msdatasource.mapper.master.SysCtlMasterDAO;
import com.cent.demo.msdatasource.mapper.slave.SysCtlSlaveDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
class DemoMsDatasourceApplicationTests {

	@Autowired
	private SysCtlMasterDAO sysCtlMasterDAO;

	@Autowired
	private SysCtlSlaveDAO sysCtlSlaveDAO;

	@Test
	void contextLoads() {
	}

	@Test
	void sysCtlDAO() {
		String name = "DataSource";
		String ctlValue = "master/slave";
		int status = 0;
		int version = 1;

		// 测试配置初始化
		SysCtlDO sysCtlDO = SysCtlDO.builder()
				.name(name)
				.ctlValue(ctlValue)
				.status(status)
				.version(version)
				.mntTime(LocalDateTime.now())
				.addTime(LocalDateTime.now())
				.build();
		log.info("初始配置：{}", sysCtlDO);

		// 测试主库新增
		int cnt = sysCtlMasterDAO.insertSysCtl(sysCtlDO);
		Assertions.assertEquals(1, cnt);
		log.info("主库新增配置");

		// 测试从库查询
		SysCtlDO sysCtlQuery = sysCtlSlaveDAO.getSysCtl(name);
		Assertions.assertNotNull(sysCtlQuery);
		Assertions.assertEquals(ctlValue, sysCtlQuery.getCtlValue());
		Assertions.assertEquals(status, sysCtlQuery.getStatus());
		Assertions.assertEquals(version, sysCtlQuery.getVersion());
		log.info("从库查询配置：{}", sysCtlQuery);

		// 测试主库更新
		status = 1;
		sysCtlDO.setStatus(status);
		sysCtlDO.setMntTime(LocalDateTime.now());
		cnt = sysCtlMasterDAO.updateSysCtlStatus(sysCtlDO);
		Assertions.assertEquals(1, cnt);
		sysCtlQuery = sysCtlSlaveDAO.getSysCtl(name);
		Assertions.assertNotNull(sysCtlQuery);
		Assertions.assertEquals(status, sysCtlQuery.getStatus());
		Assertions.assertEquals(version+1, sysCtlQuery.getVersion());
		log.info("主库更新配置：{}", sysCtlQuery);

		// 测试主库删除
		cnt = sysCtlMasterDAO.deleteSysCtl(sysCtlQuery);
		Assertions.assertEquals(1, cnt);
		List<SysCtlDO> sysCtlList = sysCtlSlaveDAO.listSysCtl();
		Assertions.assertTrue(sysCtlList.isEmpty());
		log.info("从库所有配置：{}", sysCtlList);
	}

}
