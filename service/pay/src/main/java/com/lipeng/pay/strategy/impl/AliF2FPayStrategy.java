package com.lipeng.pay.strategy.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.lipeng.alipay.config.AlipayConfig;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.strategy.PayStrategy;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliF2FPayStrategy implements PayStrategy {

	@Override
	public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO) {
		log.info(">>>>>支付宝手机支付参数封装开始<<<<<<<<");

		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);

		// 设置请求参数
		AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest ();
//		alipayRequest.setReturnUrl(AlipayConfig.return_url);
//		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		//参数从数据库获取 上面是从config获取
		alipayRequest.setReturnUrl(pymentChannel.getSyncUrl());
		alipayRequest.setNotifyUrl(pymentChannel.getAsynUrl());

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String outTradeNo = payMentTransacDTO.getPaymentId();
		// 付款金额，必填
		String totalAmount = changeF2Y(payMentTransacDTO.getPayAmount().toString());
		// 订单名称，必填
		String subject = "李鹏QB扫码充值业务";
		// 商品描述，可空
		String body = "球球比的扫码充值业务";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		model.setOutTradeNo(outTradeNo);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setBody(body);

        alipayRequest.setBizModel(model);

		// 请求
        try {
            //通过alipayClient调用API，获得对应的response类
            AlipayTradePrecreateResponse response = alipayClient.execute(alipayRequest);
            if ("Success".equals(response.getMsg())) {
                return response.getQrCode();
            }
            return null;
        } catch (Exception e) {
            log.error("alipayClient pageExecute error,request:" + alipayRequest.getBizContent(), e);
            return null;
        }

	}

	/** 金额为分的格式 */
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

	/**
	 * 将分为单位的转换为元 （除100）
	 *
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String changeF2Y(String amount) {
		if (!amount.matches(CURRENCY_FEN_REGEX)) {
			return null;
		}
		return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
	}

}