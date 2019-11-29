package com.lipeng.integral.consume;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.integral.mapper.IntegralMapper;
import com.lipeng.integral.mapper.entity.IntegralEntity;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IntegralConsumer {

    @Autowired
    private IntegralMapper integralMapper;

    //@RabbitListener(queues = "integral_queue")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "integral_queue", durable = "true"),
            exchange = @Exchange(value = "integral_exchange_name", type = "topic", ignoreDeclarationExceptions = "true"),
            key = "integralRoutingKey"))
    @RabbitHandler
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel)
            throws IOException {
        try {
            String messageId = message.getMessageProperties().getMessageId();
            String msg = new String(message.getBody(), Constants.UTF_8);
            log.info(">>>messageId:{},msg:{}", messageId, msg);
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String paymentId = jsonObject.getString("paymentId");
            if (StringUtils.isEmpty(paymentId)) {
                log.error(">>>>支付id不能为空 paymentId:{}", paymentId);
                basicNack(message, channel);
                return;
            }
            // 使用paymentId查询是否已经增加过积分 网络重试间隔
            IntegralEntity resultIntegralEntity = integralMapper.findIntegral(paymentId);
            if (resultIntegralEntity != null) {
                log.error(">>>>paymentId:{}已经增加过积分", paymentId);
                // 已经增加过积分，通知MQ不要在继续重试。
                basicNack(message, channel);
                return;
            }
            Integer userId = jsonObject.getInteger("userId");
            if (userId == null) {
                log.error(">>>>paymentId:{},对应的用户userId参数为空", paymentId);
                basicNack(message, channel);
                return;
            }
            Long integral = jsonObject.getLong("integral");
            if (integral == null) {
                log.error(">>>>paymentId:{},对应的用户integral参数为空", paymentId);
                return;
            }
            IntegralEntity integralEntity = new IntegralEntity();
            integralEntity.setPaymentId(paymentId);
            integralEntity.setIntegral(integral);
            integralEntity.setUserId(userId);
            integralEntity.setAvailability(Constants.INTEGRAL_USEABLE);
            // 插入到数据库中
            int insertIntegral = integralMapper.insertIntegral(integralEntity);
            if (insertIntegral > 0) {
                // 手动签收消息,通知mq服务器端删除该消息
                basicAck(message, channel);
            }
            // 采用重试机制
        } catch (Exception e) {
            log.error(">>>>ERROR MSG:", e);
            basicNack(message, channel);
        }

    }

    // 消费者获取到消息之后 手动签收 通知MQ删除该消息
    private void basicNack(Message message, Channel channel) throws IOException {
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }

    private void basicAck(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}
