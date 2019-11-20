package com.lipeng.elk.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class KafkaSender<T> {

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * kafka 发送消息
     *
     * @param obj 消息对象
     */
    public void send(T obj) {
        String jsonObj = JSON.toJSONString(obj);
        log.info("message = {}", jsonObj);
        // 发送消息 实现可配置化 主题是可配置化
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, jsonObj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Produce: The message failed to be sent:" + throwable.getMessage(),
                        throwable);
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //log.info("Produce: result: " + stringObjectSendResult.toString());
            }
        });
    }
}