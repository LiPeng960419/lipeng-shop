package com.lipeng.pay.callback.template.thread;

import com.lipeng.core.utils.SpringContextUtil;
import com.lipeng.pay.mapper.PaymentTransactionLogMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionLogEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lipeng 910138
 * @Date: 2020/1/2 16:51
 */
@Slf4j
public class PayLogThread implements Runnable {

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

    private void payLog(String paymentId, String verifySignature, String channelId) {
        log.info("PayLog>>>>>paymentId:{},verifySignature:{}", paymentId, verifySignature);
        PaymentTransactionLogEntity paymentTransactionLog = new PaymentTransactionLogEntity();
        PaymentTransactionLogMapper paymentTransactionLogMapper = SpringContextUtil.getBean(PaymentTransactionLogMapper.class);
        paymentTransactionLog.setTransactionId(paymentId);
        paymentTransactionLog.setAsyncLog(verifySignature);
        paymentTransactionLog.setChannelId(channelId);
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLog);
    }

}