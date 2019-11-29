package com.lipeng.pay.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.pay.config.RabbitmqConfig;
import com.lipeng.pay.mapper.BrokerMessageLogMapper;
import java.util.Date;
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

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

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
            JSONObject jsonObject = JSONObject.parseObject(correlationData.getId());
            String paymentId = jsonObject.getString("paymentId");
            log.info(">>>MQ消息确认机制投递到积分系统MQ,消息消费成功,paymentId:{}", paymentId);
            int result = brokerMessageLogMapper.changeBrokerMessageLogStatus(paymentId,
                    Constants.SEND_SUCCESS, new Date());
            if (toDaoResult(result)) {
                log.info(">>>修改MessageLog日志记录状态成功,paymentId:{}", paymentId);
            }
        }
    }

    public boolean toDaoResult(int result) {
        return result > 0;
    }

}