package com.lipeng.pay.impl.pay;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.constants.Constants;
import com.lipeng.core.token.GenerateToken;
import com.lipeng.core.twitter.SnowflakeIdUtils;
import com.lipeng.pay.dto.PayCratePayTokenDto;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.service.pay.PayMentTransacTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayMentTransacTokenServiceImpl extends BaseApiService<JSONObject> implements
		PayMentTransacTokenService {
	@Autowired
	private PaymentTransactionMapper paymentTransactionMapper;

	@Autowired
	private GenerateToken generateToken;

	@Override
	public BaseResponse<JSONObject> cratePayToken(PayCratePayTokenDto payCratePayTokenDto) {
		String orderId = payCratePayTokenDto.getOrderId();
		if (StringUtils.isEmpty(orderId)) {
			return setResultError("订单号码不能为空!");
		}
		Long payAmount = payCratePayTokenDto.getPayAmount();
		if (payAmount == null) {
			return setResultError("金额不能为空!");
		}
		Long userId = payCratePayTokenDto.getUserId();
		if (userId == null) {
			return setResultError("userId不能为空!");
		}
		// 2.将输入插入数据库中 待支付记录
		PaymentTransactionEntity paymentTransactionEntity = new PaymentTransactionEntity();
		paymentTransactionEntity.setOrderId(orderId);
		paymentTransactionEntity.setPayAmount(payAmount);
		paymentTransactionEntity.setUserId(userId);
		// 使用雪花算法 生成全局id
		paymentTransactionEntity.setPaymentId(SnowflakeIdUtils.nextId());
		int result = paymentTransactionMapper.insertPaymentTransaction(paymentTransactionEntity);
		if (!toDaoResult(result)) {
			return setResultError("系统错误!");
		}
		// 获取主键id
		Long payId = paymentTransactionEntity.getId();
		if (payId == null) {
			return setResultError("系统错误!");
		}

		// 3.生成对应支付令牌
		String keyPrefix = "pay_";
		String token = generateToken.createToken(keyPrefix, payId.toString(), Constants.PAY_TOKEN_TIME);
		JSONObject dataResult = new JSONObject();
		dataResult.put("token", token);

		return setResultSuccess(dataResult);
	}

}
