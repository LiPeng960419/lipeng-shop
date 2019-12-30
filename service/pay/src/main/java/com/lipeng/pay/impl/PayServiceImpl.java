package com.lipeng.pay.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.lipeng.alipay.config.AlipayConfig;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.core.bean.MeiteBeanUtils;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.service.PayService;
import com.lipeng.pay.utils.PayUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:16
 */
@Slf4j
@RestController
public class PayServiceImpl extends BaseApiService<JSONObject>
        implements PayService {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    //trade_no是支付宝支付id对应partyPayId
    //out_trade_no对应我本地的paymentId
    //http://127.0.0.1:8600/queryPayment?orderNo=2019112922001486281000065472
    @Override
    public BaseResponse<JSONObject> queryF2F(String paymentId) {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        //通过trade_no或者out_trade_no可以查询到订单信息
        //model.setTradeNo(orderNo);
        model.setOutTradeNo(paymentId);
        request.setBizModel(model);
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return setResultSuccess(response.getBody());
            } else {
                log.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("queryF2F error", e);
        }

        return null;
    }

    /*

     */
    @Override
    public BaseResponse<JSONObject> refund(Long id) {
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(id);
        PayMentTransacDTO dto = MeiteBeanUtils.doToDto(paymentTransaction, PayMentTransacDTO.class);

        String msg = checkParams(dto);
        if (Objects.nonNull(msg)) {
            return setResultError(msg);
        }

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setTradeNo(dto.getPartyPayId());
        model.setOutTradeNo(dto.getPaymentId());
        model.setRefundAmount(PayUtil.changeF2Y(dto.getPayAmount().toString()));
        request.setBizModel(model);
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return setResultSuccess(response.getBody());
            } else {
                log.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("refund error", e);
        }
        return null;
    }

    /*
    订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
    统一收单交易退款查询
     */
    @Override
    public BaseResponse<JSONObject> refundQuery(Long id) {
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(id);
        PayMentTransacDTO dto = MeiteBeanUtils.doToDto(paymentTransaction, PayMentTransacDTO.class);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setTradeNo(dto.getPartyPayId());
        model.setOutTradeNo(dto.getPaymentId());
        model.setOutRequestNo(dto.getPartyPayId());
        request.setBizModel(model);
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return setResultSuccess(response.getBody());
            } else {
                log.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("refundQuery error", e);
        }
        return null;
    }

    @Override
    public BaseResponse<JSONObject> cancel(Long id) {
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectById(id);
        PayMentTransacDTO dto = MeiteBeanUtils.doToDto(paymentTransaction, PayMentTransacDTO.class);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest ();
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setTradeNo(dto.getPartyPayId());
        model.setOutTradeNo(dto.getPaymentId());
        request.setBizModel(model);
        try {
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return setResultSuccess(response.getBody());
            } else {
                log.error(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("cancel error", e);
        }
        return null;
    }

    private String checkParams(PayMentTransacDTO payMentTransacDTO) {
        if (Objects.isNull(payMentTransacDTO)) {
            return "查询不到订单信息";
        }
        if (StringUtils.isEmpty(payMentTransacDTO.getPaymentId())) {
            return "查询不到订单ID";
        }
        if (StringUtils.isEmpty(payMentTransacDTO.getPartyPayId())) {
            return "查询不到第三方支付ID";
        }
        if (Objects.isNull(payMentTransacDTO.getPayAmount())) {
            return "支付金额为空";
        }
        return null;
    }

}