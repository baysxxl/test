package net.wlfeng.test.util;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author weilingfeng
 * @date 2020/4/8 16:40
 * @description
 */
public class CacheUtil {

    private CacheUtil() {}

    private static RedissonClient redissonClient;

    private static final String KEY_PERFIX = "test:";

    private static synchronized RedissonClient getInstance() {
        if (redissonClient == null) {
            redissonClient = SpringBeanUtils.getBean(RedissonClient.class);
        }
        return redissonClient;
    }

    /**
     * 获取String类型的缓存
     * @param key
     * @return
     */
    public static String getString(String key) {
        RBucket<String> bucket = getInstance().getBucket(getKey(key));
        return bucket.get();
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public static <T> T get(String key) {
        RBucket<T> bucket = getInstance().getBucket(getKey(key));
        return bucket.get();
    }

    /**
     * 缓存数据,缓存时间单位:秒
     * @param key
     * @param data
     * @param timeToLive
     * @return
     */
    public static <T> void cacheBySecond(String key, T data, long timeToLive) {
        RBucket<T> bucket = getInstance().getBucket(getKey(key));
        bucket.set(data, timeToLive, TimeUnit.SECONDS);
    }

    /**
     * 缓存数据,缓存时间单位:分
     * @param key
     * @param data
     * @param timeToLive
     * @return
     */
    public static <T> void cacheByMinutes(String key, T data, long timeToLive) {
        RBucket<T> bucket = getInstance().getBucket(getKey(key));
        bucket.set(data, timeToLive, TimeUnit.MINUTES);
    }

    /**
     * 清除缓存
     * @param key
     * @return
     */
    public static boolean delete(String key) {
        RBucket bucket = getInstance().getBucket(getKey(key));
        if (Objects.nonNull(bucket.get())) {
            return bucket.delete();
        }
        return true;
    }

    private static String getKey(String key) {
        return KEY_PERFIX + key;
    }

}
