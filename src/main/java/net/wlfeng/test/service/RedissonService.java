package net.wlfeng.test.service;

/**
 * @author weilingfeng
 * @date 2019/11/26 10:09
 * @description redisson相关服务
 */
public interface RedissonService {

    <T> void pushToDelayQueue(String key, T data);

    void takeOfDelayQueue(String key);

    <T> void put(String key, T value);

    <T> T get(String key);
}
