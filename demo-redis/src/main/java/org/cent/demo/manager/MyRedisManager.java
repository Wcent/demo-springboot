package org.cent.demo.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class MyRedisManager<T> {

    private final static Logger logger = LoggerFactory.getLogger(MyRedisManager.class);

    @Value("${redis.key-prefix.app-name:''}")
    private String redisKeyPrefixAppName;

    @Resource
    private RedisTemplate<String, T> myRedisTemplate;

    public boolean setValue(String key, T value) {
        key = redisKeyPrefixAppName + key;
        try {
            myRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception ex) {
            logger.error(String.format("设置%s缓存异常", key), ex);
            return false;
        }
    }

    public boolean setValue(String key, T value, Duration timeout) {
        key = redisKeyPrefixAppName + key;
        try {
            myRedisTemplate.opsForValue().set(key, value, timeout);
            return true;
        } catch (Exception ex) {
            logger.error(String.format("设置[%s]缓存异常", key), ex);
            return false;
        }
    }

    public T getValue(String key) {
        key = redisKeyPrefixAppName + key;
        try {
            return myRedisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            logger.error(String.format("获取[%s]缓存异常", key), ex);
            return null;
        }
    }

    public Long getExpire(String key) {
        key = redisKeyPrefixAppName + key;
        try {
            return myRedisTemplate.getExpire(key);
        } catch (Exception ex) {
            logger.error(String.format("获取[%s]缓存期限异常", key), ex);
            return -2L;
        }
    }

    public Long getExpire(String key, TimeUnit timeUnit) {
        key = redisKeyPrefixAppName + key;
        try {
            return myRedisTemplate.getExpire(key, timeUnit);
        } catch (Exception ex) {
            logger.error(String.format("获取[%s]缓存期限异常", key), ex);
            return -2L;
        }
    }

    public T getValueAndExpire(String key, Duration expire) {
        key = redisKeyPrefixAppName + key;
        try {
            return myRedisTemplate.opsForValue().getAndExpire(key, expire);
        } catch (Exception ex) {
            logger.error(String.format("获取[%s]缓存异常", key), ex);
            return null;
        }
    }

    public boolean delValue(String key) {
        key = redisKeyPrefixAppName + key;
        try {
            return Boolean.TRUE.equals(myRedisTemplate.delete(key));
        } catch (Exception ex) {
            logger.error(String.format("删除[%s]缓存异常", key), ex);
            return false;
        }
    }
}
