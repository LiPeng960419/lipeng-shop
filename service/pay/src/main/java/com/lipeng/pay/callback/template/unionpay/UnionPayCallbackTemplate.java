package com.lipeng.pay.callback.template.unionpay;

import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;
import com.lipeng.pay.constant.PayConstant;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.unionpay.acp.sdk.AcpService;
import com.lipeng.unionpay.acp.sdk.LogUtil;
import com.lipeng.unionpay.acp.sdk.SDKConstants;
import com.lipeng.unionpay.acp.sdk.UnionPayBase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UnionPayCallbackTemplate extends AbstractPayCallbackTemplate {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    /**
     * 获取请求参数中所有的信息 当商户上送frontUrl或backUrl地址中带有参数信息的时候， 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，
     * 这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    // System.out.println("======为空的字段名===="+en);
                    res.remove(en);
                }
            }
        }
        return res;
    }

    /**
     * 获取请求参数中所有的信息。 非struts可以改用此方法获取，好处是可以过滤掉request.getParameter方法过滤不掉的url中的参数。
     * struts可能对某些content-type会提前读取参数导致从inputstream读不到信息，所以可能用不了这个方法。 理论应该可以调整struts配置使不影响，但请自己去研究。
     * 调用本方法之前不能调用req.getParameter("key");这种方法，否则会导致request取不到输入流。
     */
    public static Map<String, String> getAllRequestParamStream(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        try {
            String notifyStr = new String(IOUtils.toByteArray(request.getInputStream()),
                    UnionPayBase.encoding);
            LogUtil.writeLog("收到通知报文：" + notifyStr);
            String[] kvs = notifyStr.split("&");
            for (String kv : kvs) {
                String[] tmp = kv.split("=");
                if (tmp.length >= 2) {
                    String key = tmp[0];
                    String value = URLDecoder.decode(tmp[1], UnionPayBase.encoding);
                    res.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeLog(
                    "getAllRequestParamStream.UnsupportedEncodingException error: " + e.getClass()
                            + ":"
                            + e.getMessage());
        } catch (IOException e) {
            LogUtil.writeLog("getAllRequestParamStream.IOException error: " + e.getClass() + ":" + e
                    .getMessage());
        }
        return res;
    }

    @Override
    public Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp) {

        LogUtil.writeLog("BackRcvResponse接收后台通知开始");

        String encoding = req.getParameter(SDKConstants.param_encoding);
        // 获取银联通知服务器发送的后台通知参数
        Map<String, String> reqParam = getAllRequestParam(req);
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
                        PayStrategy.UNION_PAY_CHANNEL_ID);
        // 3.使用MQ调用积分服务接口增加积分(处理幂等性问题)
        paymentTransaction.setPaymentChannel(PayStrategy.UNION_PAY_CHANNEL_ID);
        addMQIntegral(paymentTransaction);
        int x = 1/0;
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
    /**
     * 回调机制 必须遵循规范 重试机制都是采用间隔新 错开的话 必须
     */

}