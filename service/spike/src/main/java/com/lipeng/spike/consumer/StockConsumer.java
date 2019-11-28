package com.lipeng.spike.consumer;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.spike.service.mapper.OrderMapper;
import com.lipeng.spike.service.mapper.SeckillMapper;
import com.lipeng.spike.service.mapper.entity.OrderEntity;
import com.lipeng.spike.service.mapper.entity.SeckillEntity;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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

    @RabbitListener(queues = "modify_inventory_queue")
    @Transactional
    public void process(Message message) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        log.info(">>>messageId:{},msg:{}", messageId, msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        // 1.获取秒杀id
        Long seckillId = jsonObject.getLong("seckillId");
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            log.warn("seckillId:{},商品信息不存在!", seckillId);
            return;
        }
        Long version = seckillEntity.getVersion();
        int inventoryDeduction = seckillMapper.inventoryDeduction(seckillId, version);
        if (!toDaoResult(inventoryDeduction)) {
            log.info(">>>seckillId:{}修改库存失败>>>>inventoryDeduction返回为{} 秒杀失败！", seckillId,
                    inventoryDeduction);
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
            return;
        }
        log.info(">>>修改库存成功seckillId:{}>>>>秒杀成功", seckillId);
    }

    // 调用数据库层判断
    public boolean toDaoResult(int result) {
        return result > 0;
    }

}