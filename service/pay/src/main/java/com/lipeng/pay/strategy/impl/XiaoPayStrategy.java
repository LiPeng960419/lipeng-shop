package com.lipeng.pay.strategy.impl;


import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.strategy.PayStrategy;

public class XiaoPayStrategy implements PayStrategy {

	@Override
	public String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO) {
		return "小米支付from表单提交";
	}

}