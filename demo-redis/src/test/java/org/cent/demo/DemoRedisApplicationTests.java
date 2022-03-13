package org.cent.demo;

import lombok.extern.slf4j.Slf4j;
import org.cent.demo.dao.MsgDao;
import org.cent.demo.error.AppMessage;
import org.cent.demo.manager.MyRedisManager;
import org.cent.demo.model.CachePacket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
class DemoRedisApplicationTests {

	@Resource
	private MsgDao msgDao;

	@Resource
	private MyRedisManager<Object> myRedisManager;

	@Resource
	private MyRedisManager<CachePacket> myCachePacketRedisManager;

	@Test
	void contextLoads() {
	}

	@Test
	void testRedisCache() {
		log.warn("预期查表，空值");
		Assertions.assertNull(msgDao.qryMsg("SUC0000"));
		log.warn("预期查缓存，空值");
		Assertions.assertNull(msgDao.qryMsg("SUC0000"));

		msgDao.addMsg(AppMessage.SUC0000);
		log.warn("预期查缓存，非空");
		Assertions.assertEquals(AppMessage.SUC0000, msgDao.qryMsg("SUC0000"));

		msgDao.delMsg("SUC0000");
		log.warn("预期查表，空值");
		Assertions.assertNull(msgDao.qryMsg("SUC0000"));
		log.warn("预期查缓存，空值");
		Assertions.assertNull(msgDao.qryMsg("SUC0000"));
	}

	@Test
	void testMyRedis() {
		Duration expire = Duration.ofMinutes(3);

		// cache int
		Assertions.assertTrue(myRedisManager.setValue("int", 9, expire));
		Assertions.assertEquals(9, myRedisManager.getValue("int"));

		// cache null
		Assertions.assertTrue(myRedisManager.setValue("null", null, expire));
		Assertions.assertNull(myRedisManager.getValue("null"));

		// cache String
		Assertions.assertTrue(myRedisManager.setValue("hello", "redis", expire));
		Assertions.assertEquals("redis", myRedisManager.getValue("hello"));

		// cache LocalDateTime
		Assertions.assertTrue(myRedisManager.setValue("now", LocalDateTime.now(), expire));
		Assertions.assertNotNull(myRedisManager.getValue("now"));

		// cache BigDecimal
		Assertions.assertTrue(myRedisManager.setValue("decimal", new BigDecimal("1.23"), expire));
		Assertions.assertEquals(new BigDecimal("1.23"), myRedisManager.getValue("decimal"));

		// cache enum
		Assertions.assertTrue(myRedisManager.setValue(AppMessage.ERR0000.getCode(), AppMessage.ERR0000, expire));
		Assertions.assertEquals(AppMessage.ERR0000, myRedisManager.getValue(AppMessage.ERR0000.getCode()));

		// cache Object
		CachePacket cachePacket = new CachePacket(null);
		Assertions.assertTrue(myRedisManager.setValue("object", cachePacket, expire));
		Assertions.assertEquals(cachePacket, myRedisManager.getValue("object"));

		// cache packet
		cachePacket.setPayload(AppMessage.ERR0404);
		Assertions.assertTrue(myCachePacketRedisManager.setValue("CachePacket", cachePacket, expire));
		Assertions.assertEquals(cachePacket, myRedisManager.getValue("CachePacket"));

		// expire
		Assertions.assertTrue(myRedisManager.setValue("expire", "no"));
		Assertions.assertEquals(-1, myRedisManager.getExpire("expire"));
		Assertions.assertTrue(myRedisManager.setValue("expire", expire, expire));
		Assertions.assertTrue(-1 < myRedisManager.getExpire("expire"));
	}

}
