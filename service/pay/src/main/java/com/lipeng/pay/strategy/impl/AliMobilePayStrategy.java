package com.lipeng.pay.strategy.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.pay.utils.AliPayUtil;
import lombok.extern.slf4j.Slf4j;

/*文档地址
https://docs.open.alipay.com/api_1/alipay.trade.wap.pay
 */
@Slf4j
public class AliMobilePayStrategy implements PayStrategy {

	private static final String PRODUCT_CODE = "QUICK_WAP_WAY";

	@Override
	public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO) {
		log.info(">>>>>支付宝手机支付参数封装开始<<<<<<<<");

		// 获得初始化的AlipayClient
		AlipayClient alipayClient = AliPayUtil.getAlipayClient();

		// 设置请求参数
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
//		alipayRequest.setReturnUrl(AlipayConfig.return_url);
//		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		//参数从数据库获取 上面是从config获取
		alipayRequest.setReturnUrl(pymentChannel.getSyncUrl());
		alipayRequest.setNotifyUrl(pymentChannel.getAsynUrl());

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String outTradeNo = payMentTransacDTO.getPaymentId();
		// 付款金额，必填
		String totalAmount = AliPayUtil.changeF2Y(payMentTransacDTO.getPayAmount().toString());
		// 订单名称，必填
		String subject = "李鹏QB充值业务";
		// 商品描述，可空
		String body = "球球比的充值业务";

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(outTradeNo);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setBody(body);
		model.setProductCode(PRODUCT_CODE);

        alipayRequest.setBizModel(model);

		// 请求
		try {
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			return result;
		} catch (Exception e) {
			log.error("alipayClient pageExecute error,request:" + alipayRequest.getBizContent(), e);
			return null;
		}

	}

}