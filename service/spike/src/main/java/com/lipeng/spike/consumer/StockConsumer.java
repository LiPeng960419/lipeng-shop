package com.lipeng.spike.consumer;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.spike.service.mapper.OrderMapper;
import com.lipeng.spike.service.mapper.SeckillMapper;
import com.lipeng.spike.service.mapper.entity.OrderEntity;
import com.lipeng.spike.service.mapper.entity.SeckillEntity;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存消费者
 */
@Component
@Slf4j
public class StockConsumer {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private OrderMapper orderMapper;

    //@RabbitListener(queues = "modify_inventory_queue")
    @Transactional
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "modify_inventory_queue", durable = "true"),
            exchange = @Exchange(value = "modify_exchange_name", type = "topic", ignoreDeclarationExceptions = "true"),
            key = "modifyRoutingKey"))
    @RabbitHandler
    public void process(Message message, Channel channel) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), Constants.UTF_8);
        log.info(">>>messageId:{},msg:{}", messageId, msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        // 1.获取秒杀id
        Long seckillId = jsonObject.getLong("seckillId");
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            log.warn("seckillId:{},商品信息不存在!", seckillId);
            basicNack(message, channel);
            return;
        }
        Long version = seckillEntity.getVersion();
        int inventoryDeduction = seckillMapper.inventoryDeduction(seckillId, version);
        if (!toDaoResult(inventoryDeduction)) {
            log.info(">>>seckillId:{}修改库存失败>>>>inventoryDeduction返回为{} 秒杀失败！", seckillId,
                    inventoryDeduction);
            basicNack(message, channel);
            return;
        }
        // 2.添加秒杀订单
        OrderEntity orderEntity = new OrderEntity();
        String phone = jsonObject.getString("phone");
        orderEntity.setUserPhone(phone);
        orderEntity.setSeckillId(seckillId);
        orderEntity.setState(1);
        int insertOrder = orderMapper.insertOrder(orderEntity);
        if (!toDaoResult(insertOrder)) {
            basicNack(message, channel);
            return;
        }
        basicAck(message, channel);
        log.info(">>>修改库存成功seckillId:{}>>>>秒杀成功", seckillId);
    }

    // 调用数据库层判断
    public boolean toDaoResult(int result) {
        return result > 0;
    }

    private void basicNack(Message message, Channel channel) throws IOException {
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }

    private void basicAck(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}