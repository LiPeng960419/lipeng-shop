package com.lipeng.pay.callback.template.alipay;

import com.alipay.api.internal.util.AlipaySignature;
import com.lipeng.alipay.config.AlipayConfig;
import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;
import com.lipeng.pay.constant.PayConstant;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/15 9:58
 */
@Component
@Slf4j
public class AliPayCallbackTemplate extends AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Override
    public Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> requestParams = req.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

    @Override
    public String asyncService(Map<String, String> params) {
        // 获取支付宝GET过来反馈信息
        try {
            log.info("####Alipay异步回调开始####{}:", params);
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
            //String trade_no = params.get("trade_no");
            // 交易状态
            String trade_status = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(trade_status)) {
                paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS.toString(), outTradeNo);
            } else {
                return PayConstant.ALI_RESULT_FAIL;
            }
            return PayConstant.ALI_RESULT_SUCCESS;
        } catch (Exception e) {
            log.info("######PayCallBackServiceImpl asynCallBack##ERROR:#####{}", e);
            return PayConstant.ALI_RESULT_FAIL;
        } finally {
            log.info("####Alipay异步回调结束####{}:", params);
        }
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