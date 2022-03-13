package org.cent.demo.dao;

import lombok.extern.slf4j.Slf4j;
import org.cent.demo.error.AppMessage;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@CacheConfig(cacheNames = "redis-cache")
@Slf4j
public class MsgDao {

    private final ConcurrentHashMap<String, AppMessage> mdb = new ConcurrentHashMap<>();

    @CachePut(key = "#msg.code")
    public AppMessage addMsg(AppMessage msg) {
        log.debug("模拟写表，写缓存");
        mdb.put(msg.getCode(), msg);
        return msg;
    }

    @CacheEvict(key = "#code")
    public void delMsg(String code) {
        log.debug("模拟清表，清缓存");
        mdb.remove(code);
    }

    @Cacheable(key = "#code")
    public AppMessage qryMsg(String code) {
        log.debug("模拟查表，记缓存");
        return mdb.getOrDefault(code, null);
    }
}
