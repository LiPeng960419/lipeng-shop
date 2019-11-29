package com.lipeng.pay.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqConfig {

    // 积分交换机
    public static final String INTEGRAL_EXCHANGE_NAME = "integral_exchange_name";
    // 添加积分队列
    public static final String INTEGRAL_DIC_QUEUE = "integral_queue";
    //添加积分路由
    public static final String INTEGRAL_ROUTING_KEY = "integralRoutingKey";

    // 支付补偿交换机
    public static final String PAY_EXCHANGE_NAME = "pay_exchange_name";
    // 支付补偿队列
    public static final String PAY_DIC_QUEUE = "pay_queue";
    //支付补偿路由
    public static final String PAY_ROUTING_KEY = "payRoutingKey";

    // 1.添加积分队列
    @Bean
    public Queue integralQueue() {
        return new Queue(INTEGRAL_DIC_QUEUE);
    }

    // 2.定义积分交换机
    @Bean
    DirectExchange integralExchange() {
        return new DirectExchange(INTEGRAL_EXCHANGE_NAME);
    }

    // 3.积分队列与交换机绑定
    @Bean
    Binding bindingExchangeintegralDicQueue() {
        return BindingBuilder.bind(integralQueue()).to(integralExchange())
                .with(INTEGRAL_ROUTING_KEY);
    }

    // 1.定义支付补偿队列
    @Bean
    public Queue payQueue() {
        return new Queue(PAY_DIC_QUEUE);
    }

    // 2.定义支付补偿交换机
    @Bean
    DirectExchange payDirectExchange() {
        return new DirectExchange(PAY_EXCHANGE_NAME);
    }

    // 3.补偿队列与交换机绑定
    @Bean
    Binding bindingExchangeCreateintegral() {
        return BindingBuilder.bind(payQueue()).to(payDirectExchange()).with(PAY_ROUTING_KEY);
    }

    @Bean("payRabbitTemplate")
    public RabbitTemplate payRabbitTemplate(CachingConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        /**
         * 当mandatory标志位设置为true时
         * 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
         * 那么broker会调用basic.return方法将消息返还给生产者
         * 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
         */
        rabbitTemplate.setMandatory(true);
        //使用单独的发送连接，避免生产者由于各种原因阻塞而导致消费者同样阻塞
        rabbitTemplate.setUsePublisherConnection(true);
        return rabbitTemplate;
    }

    @Bean("integralRabbitTemplate")
    public RabbitTemplate integralRabbitTemplate(CachingConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setUsePublisherConnection(true);
        return rabbitTemplate;
    }

}