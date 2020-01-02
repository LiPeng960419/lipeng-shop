package com.lipeng.pay.callback.template.unionpay;

import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;
import com.lipeng.pay.constant.PayConstant;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.pay.utils.UnionPayUtil;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.LogUtil;
import com.lipeng.unionpay.acp.sdk.SDKConstants;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UnionF2FPayCallbackTemplate extends AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Override
    public Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) {

        LogUtil.writeLog("BackRcvResponse接收后台通知开始");

        String encoding = req.getParameter(SDKConstants.param_encoding);
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> reqParam = UnionPayUtil.getAllRequestParam(req);
        LogUtil.printRequestLog(reqParam);

        // 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(reqParam, encoding)) {
            LogUtil.writeLog("验证签名结果[失败].");
            reqParam.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_201);
        } else {
            LogUtil.writeLog("验证签名结果[成功].");
            // 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
            String orderId = reqParam.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
            reqParam.put("paymentId", orderId);
            reqParam.put(PayConstant.RESULT_NAME, PayConstant.RESULT_PAYCODE_200);
        }
        LogUtil.writeLog("BackRcvResponse接收后台通知结束");
        return reqParam;
    }

    // 异步回调中网络尝试延迟，导致异步回调重复执行 可能存在幂等性问题
    @Override
    @Transactional
    public String asyncService(Map<String, String> verifySignature) throws Exception{

        String orderId = verifySignature.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
        String respCode = verifySignature.get("respCode");
        String tradeNo = verifySignature.get("queryId");

        // 判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
        // 1.判断respCode是否为已经支付成功断respCode=00、A6后，
        if (!("00".equals(respCode) || "A6".equals(respCode))) {
            return failResult();
        }
        // 根据日志 手动补偿 使用支付id调用第三方支付接口查询
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(orderId);
        if (PayConstant.PAY_STATUS_SUCCESS.equals(paymentTransaction.getPaymentStatus())) {
            // 网络重试中，之前已经支付过
            return successResult();
        }
        // 2.将状态改为已经支付成功
        paymentTransactionMapper.updatePaymentStatus(PayConstant.PAY_STATUS_SUCCESS.toString(),tradeNo, orderId,
                        PayStrategy.UNION_F2F_PAY_CHANNEL_ID);
        // 3.使用MQ调用积分服务接口增加积分(处理幂等性问题)
        paymentTransaction.setPaymentChannel(PayStrategy.UNION_F2F_PAY_CHANNEL_ID);
        addMQIntegral(paymentTransaction);

        return successResult();
    }

    @Override
    public String failResult() {
        return PayConstant.YINLIAN_RESULT_FAIL;
    }

    @Override
    public String successResult() {
        return PayConstant.YINLIAN_RESULT_SUCCESS;
    }

}