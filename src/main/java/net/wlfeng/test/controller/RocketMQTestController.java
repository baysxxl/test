package net.wlfeng.test.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.constant.RocketMQConstant;
import net.wlfeng.test.dto.CommonResponse;
import net.wlfeng.test.dto.MqMessage;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weilingfeng
 * @date 2022/6/28 18:50
 * @description rocketmq相关功能测试controller
 */
@Slf4j
@Controller
@RequestMapping("rocketMqTest")
public class RocketMQTestController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @ResponseBody
    @PostMapping("/sendMsg")
    public CommonResponse sendMsg(@RequestBody JSONObject value) {
        log.info("======发送消息到MQ队列:{}======", value);
        MqMessage message = MqMessage.builder().name("测试消息").msg(value.getString("msg")).build();
        SendResult sendResult = rocketMQTemplate.syncSend(RocketMQConstant.common_topic, message);
        return CommonResponse.success(sendResult);
    }

}
