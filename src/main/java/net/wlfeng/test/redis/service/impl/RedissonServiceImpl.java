package net.wlfeng.test.redis.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.redis.service.RedissonService;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author weilingfeng
 * @date 2019/11/26 10:09
 * @description
 */
@Slf4j
@Service
public class RedissonServiceImpl implements RedissonService {

    @Autowired
    private RedissonClient redissonClient;

    private long EXPIRE_TIME_MILLIS = 300000;

    @Async
    @Override
    public <T> void pushToDelayQueue(String key, T data) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingDeque(key);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(data, EXPIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Async
    @Override
    public void takeOfDelayQueue(String key) {
        log.info("======延迟队列:[{}]-消费初始化======", key);
        RBlockingQueue<String> blockingFairQueue = redissonClient.getBlockingDeque(key);
        while (true) {
            try {
               String data = blockingFairQueue.take();
               log.info("======延迟队列:[{}]-获取到数据:{}======", key, data);
            } catch (InterruptedException e) {
                log.error("延迟队列处理异常,异常信息:{}", e.getMessage());
            }
        }
    }

    @Override
    public <T> void put(String key, T value) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        rBucket.set(value);
    }

    @Override
    public <T> T get(String key) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }
}
