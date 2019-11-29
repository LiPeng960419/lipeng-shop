package com.lipeng.pay.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.pay.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class IntegralProducer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void send(JSONObject jsonObject) {
        String jsonString = jsonObject.toJSONString();
        String paymentId = jsonObject.getString("paymentId");
        // 封装消息
        Message message = MessageBuilder.withBody(jsonString.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(Constants.UTF_8)
                .setMessageId(paymentId)
                .build();
        // 构建回调返回的数据（消息id）
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(jsonString);
        rabbitTemplate.convertAndSend(RabbitmqConfig.INTEGRAL_EXCHANGE_NAME,
                RabbitmqConfig.INTEGRAL_ROUTING_KEY, message,
                correlationData);
    }

    // 生产消息确认机制 生产者往服务器端发送消息的时候，采用应答机制
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info(">>>使用MQ消息确认机制确保消息投递到积分系统MQ中成功");
        }
    }

}