package com.lipeng.spike.producer;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.spike.config.RabbitmqConfig;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 秒杀生产者
 */
@Component
@Slf4j
public class SpikeCommodityProducer implements RabbitTemplate.ConfirmCallback,
        RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void send(JSONObject jsonObject) {

        String jsonString = jsonObject.toJSONString();
        String messAgeId = UUID.randomUUID().toString().replace("-", "");
        // 封装消息
        Message message = MessageBuilder.withBody(jsonString.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(Constants.UTF_8)
                .setMessageId(messAgeId)
                .build();
        // 构建回调返回的数据（消息id）
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(jsonString);
        rabbitTemplate.convertAndSend(RabbitmqConfig.MODIFY_EXCHANGE_NAME,
                RabbitmqConfig.MODIFY_ROUTING_KEY, message,
                correlationData);
    }

    // 生产消息确认机制 生产者往服务器端发送消息的时候，采用应答机制
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info(">>>使用MQ消息确认机制确保秒杀消息投递到MQ中成功");
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
            String routingKey) {
        log.info("秒杀消息:return exchange:" + exchange + ", routingKey:" + routingKey +
                ", replyText:" + replyText);
    }

}