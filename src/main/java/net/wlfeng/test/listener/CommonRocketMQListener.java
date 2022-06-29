package net.wlfeng.test.listener;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.constant.RocketMQConstant;
import net.wlfeng.test.dto.MqMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
  * 在RocketMQ中消费者和发送者组没有关系
  * 如果两个消费者groupname和topic都一样，则二者轮循接收消息
  * 如果两个消费者topic一样，而group不一样，则消息变成广播机制
  * RocketMQListener<>泛型必须和接收的消息类型相同
  */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketMQConstant.common_topic, //topic：消息的发送者使用同一个topic
        consumerGroup = "common_consumer_a_group", //group：不用和生产者group相同
        selectorExpression = "*") //tag
public class CommonRocketMQListener implements RocketMQListener<MqMessage> {

    @Override
    public void onMessage(MqMessage message) {
        log.info("{}收到消息：{}", this.getClass().getSimpleName(), message);
    }

}
