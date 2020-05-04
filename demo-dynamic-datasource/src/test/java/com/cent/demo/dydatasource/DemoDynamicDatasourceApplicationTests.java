package com.cent.demo.dydatasource;

import com.cent.demo.dydatasource.domain.SysCtlDO;
import com.cent.demo.dydatasource.mapper.read.SysCtlReadableDAO;
import com.cent.demo.dydatasource.mapper.write.SysCtlWritableDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
class DemoDynamicDatasourceApplicationTests {

	@Autowired
	private SysCtlReadableDAO sysCtlReadableDAO;

	@Autowired
	private SysCtlWritableDAO sysCtlWritableDAO;

	@Test
	void contextLoads() {
	}

	@Test
	void sysCtlDAO() {
		String name = "DataSource";
		String ctlValue = "readable/writable";
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

		// 测试写库新增
		int cnt = sysCtlWritableDAO.insertSysCtl(sysCtlDO);
		Assertions.assertEquals(1, cnt);
		log.info("写库新增配置");

		// 测试写库查询
		SysCtlDO sysCtlQuery = sysCtlWritableDAO.getSysCtl(name);
		Assertions.assertNotNull(sysCtlQuery);
		Assertions.assertEquals(ctlValue, sysCtlQuery.getCtlValue());
		Assertions.assertEquals(status, sysCtlQuery.getStatus());
		Assertions.assertEquals(version, sysCtlQuery.getVersion());
		log.info("写库查询配置：{}", sysCtlQuery);

		// 测试读库查询所有
		List<SysCtlDO> sysCtlList = sysCtlReadableDAO.listSysCtl();
		Assertions.assertFalse(sysCtlList.isEmpty());
		log.info("读库查询所有配置：{}", sysCtlList);

		// 测试写库更新，读库查询
		status = 1;
		sysCtlDO.setStatus(status);
		sysCtlDO.setMntTime(LocalDateTime.now());
		cnt = sysCtlWritableDAO.updateSysCtlStatus(sysCtlDO);
		Assertions.assertEquals(1, cnt);
		sysCtlQuery = sysCtlReadableDAO.getSysCtl(name);
		Assertions.assertNotNull(sysCtlQuery);
		Assertions.assertEquals(status, sysCtlQuery.getStatus());
		Assertions.assertEquals(version+1, sysCtlQuery.getVersion());
		log.info("写库更新，读库查询配置：{}", sysCtlQuery);

		// 测试写库删除，写库查询所有
		cnt = sysCtlWritableDAO.deleteSysCtl(sysCtlQuery);
		Assertions.assertEquals(1, cnt);
		sysCtlList = sysCtlWritableDAO.listSysCtl();
		Assertions.assertTrue(sysCtlList.isEmpty());
		log.info("写库删除，写库查询所有配置：{}", sysCtlList);
	}
}
