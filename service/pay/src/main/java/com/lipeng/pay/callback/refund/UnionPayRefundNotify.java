package com.lipeng.pay.callback.refund;

import com.lipeng.pay.callback.template.thread.PayLogThread;
import com.lipeng.pay.common.exception.CommonPayException;
import com.lipeng.pay.constant.PayConstant;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.utils.UnionPayUtil;
import com.lipeng.unionpay.acp.sdk.LogUtil;
import com.lipeng.unionpay.acp.sdk.SDKConstants;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2020/1/2 16:28
 */
@RestController
@Slf4j
@RequestMapping("/unionPay")
public class UnionPayRefundNotify {

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @PostMapping("/refund")
    public void refundNotify(HttpServletRequest req, HttpServletResponse resp) {
        String paymentId = null;
        try {
            LogUtil.writeLog("unionPay refund接收后台通知开始");
            String encoding = req.getParameter(SDKConstants.param_encoding);
            Map<String, String> reqParam = UnionPayUtil.checkResponse(UnionPayUtil.getAllRequestParam(req), encoding);
            paymentId = reqParam.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
            PaymentTransactionEntity entity = paymentTransactionMapper.selectByPaymentId(paymentId);
            if (Objects.isNull(entity)) {
                throw new CommonPayException("银联退款未找到对应的订单信息");
            }
            paymentTransactionMapper.updatePaymentStatusByPaymentId(String.valueOf(PayConstant.PAY_STATUS_REFUND_SUCCESS), entity.getPaymentId());
            taskExecutor.execute(new PayLogThread(entity.getPaymentId(), reqParam.toString(), entity.getPaymentChannel()));
            LogUtil.writeLog("unionPay refund接收后台通知结束");
            //返回给银联服务器http 200  状态码
            resp.getWriter().print("ok");
        } catch (Exception e) {
            if (StringUtils.isNotEmpty(paymentId)) {
                paymentTransactionMapper.updatePaymentStatusByPaymentId(String.valueOf(PayConstant.PAY_STATUS_REFUND_FAIL), paymentId);
            }
            log.error(e.getMessage(), e);
        }
    }

}