package net.wlfeng.test.redis.service;

/**
 * @author weilingfeng
 * @date 2019/11/26 10:09
 * @description
 */
public interface RedissonService {

    <T> void pushToDelayQueue(String key, T data);

    void takeOfDelayQueue(String key);

    <T> void put(String key, T value);

    <T> T get(String key);
}
