package com.lipeng.pay.consumer;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.pay.constant.PayConstant;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
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
public class PayCheckStateConsumer {

	@Autowired
	private PaymentTransactionMapper paymentTransactionMapper;

	// 死信队列（备胎） 消息被拒绝、队列长度满了 定时任务 人工补偿

	//@RabbitListener(queues = "integral_create_queue")
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "integral_create_queue", durable = "true"),
			exchange = @Exchange(value = "integral_exchange_name", type = "topic", ignoreDeclarationExceptions = "true"),
			key = "integralRoutingKey"))
	@RabbitHandler
	public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
		try {
			String messageId = message.getMessageProperties().getMessageId();
			String msg = new String(message.getBody(), "UTF-8");
			log.info(">>>messageId:{},msg:{}", messageId, msg);
			JSONObject jsonObject = JSONObject.parseObject(msg);
			String paymentId = jsonObject.getString("paymentId");
			if (StringUtils.isEmpty(paymentId)) {
				log.error(">>>>支付id不能为空 paymentId:{}", paymentId);
				basicNack(message, channel);
				return;
			}
			// 1.使用paymentId查询之前是否已经支付过
			PaymentTransactionEntity paymentTransactionEntity = paymentTransactionMapper.selectByPaymentId(paymentId);
			if (paymentTransactionEntity == null) {
				log.error(">>>>支付id paymentId:{} 未查询到支付详情", paymentId);
				basicNack(message, channel);
				return;
			}
			Integer paymentStatus = paymentTransactionEntity.getPaymentStatus();
			if (PayConstant.PAY_STATUS_SUCCESS.equals(paymentStatus)) {
				log.info(">>>>支付id paymentId:{}已支付成功,支付回调检查无异常", paymentId);
				basicNack(message, channel);
				return;
			}
			// 安全期间 主动调用第三方接口查询
			String paymentChannel = jsonObject.getString("paymentChannel");
			int updatePaymentStatus = paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS.toString(), null, paymentId, paymentChannel);
			if (updatePaymentStatus > 0) {
				basicAck(message, channel);
			}
		} catch (Exception e) {
			basicNack(message, channel);
			log.error("PayCheckStateConsumer error", e);
		}
	}

	private void basicNack(Message message, Channel channel) throws IOException {
		channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
	}

	private void basicAck(Message message, Channel channel) throws IOException {
		channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
	}

}