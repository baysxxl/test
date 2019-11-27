package net.wlfeng.test.controller;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.CommonResponse;
import net.wlfeng.test.redis.constant.KeyConstant;
import net.wlfeng.test.redis.service.RedissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author weilingfeng
 * @date 2019/11/26 10:03
 * @description
 */
@Slf4j
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private RedissonService redissonService;

    @ResponseBody
    @PostMapping("/redisson/delayQueue")
    public CommonResponse delayQueueTest(@RequestBody String value) {
        log.info("======推送信息到延迟队列,信息内容:{}======", value);
        redissonService.pushToDelayQueue(KeyConstant.DELAY_QUEUE_KEY, value);
        return CommonResponse.success("ok");
    }

    @ResponseBody
    @PostMapping("/redisson/put")
    public CommonResponse redissonPutTest(@RequestParam("key") String key, @RequestParam("value") String value) {
        log.info("======推送信息到redisson,key:{},value:{}======", key, value);
        redissonService.put(key, value);
        return CommonResponse.success("ok");
    }

    @ResponseBody
    @GetMapping("/redisson/get")
    public CommonResponse redissonGetTest(@RequestParam("key") String key) {
        log.info("======从redisson获取内容,key:{}======", key);
        return CommonResponse.success(redissonService.get(key));
    }

}
