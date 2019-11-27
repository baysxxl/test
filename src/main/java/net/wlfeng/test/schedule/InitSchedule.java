package net.wlfeng.test.schedule;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.redis.constant.KeyConstant;
import net.wlfeng.test.redis.service.RedissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author weilingfeng
 * @date 2019/11/26 10:28
 * @description
 */
@Slf4j
@Component
public class InitSchedule {

    @Autowired
    private RedissonService redissonService;

    @PostConstruct
    public void init() {
        log.info("======初始化任务-开始======");
        redissonService.takeOfDelayQueue(KeyConstant.DELAY_QUEUE_KEY);
        log.info("======初始化任务-结束======");
    }

}
