package com.lipeng.pay.callback.template;

import com.lipeng.pay.mapper.PaymentTransactionLogMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionLogEntity;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionLogMapper paymentTransactionLogMapper;

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
    public abstract String asyncService(Map<String, String> verifySignature);

    public abstract String failResult();

    public abstract String successResult();

    /**
     * *1. 将报文数据存放到es <br> 1. 验证报文参数<br> 2. 将日志根据支付id存放到数据库中<br> 3. 执行的异步回调业务逻辑<br>
     */
    public String asyncCallBack(HttpServletRequest req, HttpServletResponse resp) {
        // 1. 验证报文参数 相同点 获取所有的请求参数封装成为map集合 并且进行参数验证
        Map<String, String> verifySignature = verifySignature(req, resp);
        // 2.将日志根据支付id存放到数据库中 out_trade_no对应paymentId
        String paymentId = verifySignature.get("out_trade_no");
        if (StringUtils.isEmpty(paymentId)) {
            return failResult();
        }
        // 3.采用异步形式写入日志到数据库中
        taskExecutor.execute(new PayLogThread(paymentId, verifySignature));

        // 4.执行的异步回调业务逻辑
        return asyncService(verifySignature);
    }

    /**
     * 采用多线程技术或者MQ形式进行存放到数据库中
     */
    private void payLog(String paymentId, Map<String, String> verifySignature) {
        log.info("PayLog>>>>>paymentId:{},verifySignature:{}", paymentId, verifySignature);
        PaymentTransactionLogEntity paymentTransactionLog = new PaymentTransactionLogEntity();
        paymentTransactionLog.setTransactionId(paymentId);
        paymentTransactionLog.setAsyncLog(verifySignature.toString());
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLog);
    }

    class PayLogThread implements Runnable {

        private String paymentId;
        private Map<String, String> verifySignature;

        public PayLogThread(String paymentId, Map<String, String> verifySignature) {
            this.paymentId = paymentId;
            this.verifySignature = verifySignature;
        }

        @Override
        public void run() {
            payLog(paymentId, verifySignature);
        }

    }

}