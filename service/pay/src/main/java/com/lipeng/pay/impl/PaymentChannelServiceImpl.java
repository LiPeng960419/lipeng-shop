package com.lipeng.pay.impl;

import com.lipeng.base.BaseApiService;
import com.lipeng.core.mapper.MapperUtils;
import com.lipeng.pay.dto.PaymentChannelDTO;
import com.lipeng.pay.mapper.PaymentChannelMapper;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.service.PaymentChannelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentChannelServiceImpl extends BaseApiService<List<PaymentChannelDTO>>
		implements PaymentChannelService {
	@Autowired
	private PaymentChannelMapper paymentChannelMapper;

	@Override
	public List<PaymentChannelDTO> selectAll() {
		List<PaymentChannelEntity> paymentChanneList = paymentChannelMapper.selectAll();
		return MapperUtils.mapAsList(paymentChanneList, PaymentChannelDTO.class);
	}

}
