package com.lipeng.pay.callback.template.alipay;

import com.alipay.api.internal.util.AlipaySignature;
import com.lipeng.alipay.config.AlipayConfig;
import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;
import com.lipeng.pay.constant.PayConstant;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.pay.utils.AliPayUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/15 9:58
 */
@Component
@Slf4j
public class AliF2FPayCallbackTemplate extends AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Override
    public Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) {
        return AliPayUtil.verifySignature(req);
    }

    @Override
    @Transactional
    public String asyncService(Map<String, String> params) throws Exception {
        // 获取支付宝GET过来反馈信息
        log.info("####AliF2Fpay异步回调开始####{}:", params);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
                AlipayConfig.charset, AlipayConfig.sign_type); // 调用SDK验证签名
        // ——请在这里编写您的程序（以下代码仅作参考）——
        if (!signVerified) {
            return PayConstant.ALI_RESULT_FAIL;
        }
        // 商户订单号
        String outTradeNo = params.get("out_trade_no");
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(outTradeNo);
        if (paymentTransaction == null) {
            return PayConstant.ALI_RESULT_FAIL;
        }
        if (PayConstant.PAY_STATUS_SUCCESS.equals(paymentTransaction.getPaymentStatus())) {
            // 网络重试中，之前已经支付过
            return successResult();
        }
        // 支付宝交易号
        String tradeNo = params.get("trade_no");
        // 交易状态
        String trade_status = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(trade_status)) {
            paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS.toString(), tradeNo,
                            outTradeNo, PayStrategy.ALI_F2F_PAY_CHANNEL_ID);
        } else {
            return PayConstant.ALI_RESULT_FAIL;
        }
        // 3.使用MQ调用积分服务接口增加积分(处理幂等性问题)
        paymentTransaction.setPaymentChannel(PayStrategy.ALI_F2F_PAY_CHANNEL_ID);
        addMQIntegral(paymentTransaction);

        return PayConstant.ALI_RESULT_SUCCESS;
    }

    @Override
    public String failResult() {
        return PayConstant.ALI_RESULT_FAIL;
    }

    @Override
    public String successResult() {
        return PayConstant.ALI_RESULT_SUCCESS;
    }

}