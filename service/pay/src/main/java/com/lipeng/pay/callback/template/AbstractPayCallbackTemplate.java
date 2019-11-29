package com.lipeng.pay.callback.template;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.pay.mapper.BrokerMessageLogMapper;
import com.lipeng.pay.mapper.PaymentTransactionLogMapper;
import com.lipeng.pay.mapper.entity.BrokerMessageLog;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.mapper.entity.PaymentTransactionLogEntity;
import com.lipeng.pay.mq.producer.IntegralProducer;
import com.lipeng.pay.mq.producer.PayCheckProducer;
import com.lipeng.pay.strategy.PayStrategy;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public abstract class AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionLogMapper paymentTransactionLogMapper;

    @Autowired
    private IntegralProducer integralProducer;

    @Autowired
    private PayCheckProducer payCheckProducer;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private Executor taskExecutor;

    /**
     * 获取所有请求的参数，封装成Map集合 并且验证是否被篡改
     */
    public abstract Map<String, String> verifySignature(
            HttpServletRequest req, HttpServletResponse resp);

    /**
     * 异步回调执行业务逻辑
     */
    @Transactional
    public abstract String asyncService(Map<String, String> verifySignature)
            throws Exception;

    public abstract String failResult();

    public abstract String successResult();

    /**
     * *1. 将报文数据存放到es <br> 1. 验证报文参数<br> 2. 将日志根据支付id存放到数据库中<br> 3. 执行的异步回调业务逻辑<br>
     */
    @Transactional
    public String asyncCallBack(HttpServletRequest req, HttpServletResponse resp, String channelId)
            throws Exception {
        // 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
        Map<String, String> verifySignature = verifySignature(req, resp);
        // 2.将日志根据支付id存放到数据库中
        String paymentId = "";
        if (PayStrategy.ALI_PAY_CHANNEL_ID.equals(channelId)) {
            paymentId = verifySignature.get("out_trade_no");
        } else if (PayStrategy.UNION_PAY_CHANNEL_ID.equals(channelId)) {
            paymentId = verifySignature.get("paymentId");
        }
        if (StringUtils.isEmpty(paymentId)) {
            log.error("asyncCallBack paymentId isEmpty");
            return failResult();
        }
        String log = verifySignature.toString();
        // 3.采用异步形式写入日志到数据库中
        taskExecutor.execute(new PayLogThread(paymentId, log, channelId));

        // 4.执行的异步回调业务逻辑
        return asyncService(verifySignature);
    }

    /**
     * 基于MQ增加积分以及支付补偿检查
     */
    @Async
    public void addMQIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        //自定义积分加多少
        jsonObject.put("integral", paymentTransaction.getPayAmount());
        jsonObject.put("paymentChannel", paymentTransaction.getPaymentChannel());
        // 使用当前时间当做创建时间
        Date now = new Date();
        // 插入消息记录表数据
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        // 消息唯一ID
        brokerMessageLog.setMessageId(paymentTransaction.getPaymentId());
        // 保存消息整体 转为JSON 格式存储入库
        brokerMessageLog.setMessage(jsonObject.toJSONString());
        // 设置消息状态为0 表示发送中
        brokerMessageLog.setStatus(Constants.SENDING);
        // 设置消息未确认超时时间窗口为 一分钟
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(now, Constants.INTEGRAL_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.insert(brokerMessageLog);
        integralProducer.send(jsonObject);
        payCheckProducer.send(jsonObject);
    }

    /**
     * 采用多线程技术或者MQ形式进行存放到数据库中
     */
    private void payLog(String paymentId, String verifySignature, String channelId) {
        log.info("PayLog>>>>>paymentId:{},verifySignature:{}", paymentId, verifySignature);
        PaymentTransactionLogEntity paymentTransactionLog = new PaymentTransactionLogEntity();
        paymentTransactionLog.setTransactionId(paymentId);
        paymentTransactionLog.setAsyncLog(verifySignature);
        paymentTransactionLog.setChannelId(channelId);
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLog);
    }

    class PayLogThread implements Runnable {

        private String paymentId;
        private String channelId;
        private String verifySignature;

        public PayLogThread(String paymentId, String verifySignature, String channelId) {
            this.paymentId = paymentId;
            this.verifySignature = verifySignature;
            this.channelId = channelId;
        }

        @Override
        public void run() {
            payLog(paymentId, verifySignature, channelId);
        }

    }

}