# rabbitmq-multiple-spring-boot-starter
用于解决springboot连接多个rabbitmq的starter
package com.cdls.carp.open.consumer;

import com.alibaba.fastjson.JSONObject;
import com.cdls.carp.common.constant.RabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author :pu
 * @description :
 * @create :2021-06-22 11:30:00
 */
@Component
@Slf4j
public class RabbitSecondConsumer {

    /**
     * 监听中心mq的人员同步队列
     *
     * @param message 消息
     * @param channel 连接通道
     * @throws IOException
                                                             //注意这里
     */
    @RabbitListener(queues = "testQueue", containerFactory = "secondRabbitListenerContainerFactory")
    public void test(Message message, Channel channel) throws IOException {
        String json = new String(message.getBody(), "utf-8");
        log.info("消息 json={}", json);
        //ack确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        
    }

   
}
