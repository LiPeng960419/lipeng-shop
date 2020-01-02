package com.lipeng.pay.strategy.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.strategy.PayStrategy;
import com.lipeng.pay.utils.AliPayUtil;
import lombok.extern.slf4j.Slf4j;

/*
商户通过该接口进行交易的创建下单
返回二维码客户端
暂未实现回调接口
 */
@Slf4j
public class AliF2FPayStrategy implements PayStrategy {

	@Override
	public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO) {
		log.info(">>>>>支付宝扫码支付参数封装开始<<<<<<<<");

		// 获得初始化的AlipayClient
		AlipayClient alipayClient = AliPayUtil.getAlipayClient();

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
		String totalAmount = AliPayUtil.changeF2Y(payMentTransacDTO.getPayAmount().toString());
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
			if (response.isSuccess()) {
				return response.getQrCode();
			}
			return null;
		} catch (Exception e) {
			log.error("alipayClient execute error,request:" + alipayRequest.getBizContent(), e);
			return null;
		}

	}

}