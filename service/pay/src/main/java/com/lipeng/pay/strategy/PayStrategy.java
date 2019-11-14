package com.lipeng.pay.strategy;

import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;

public interface PayStrategy {

    /**
     * @param pymentChannel 渠道参数
     * @param payMentTransacDTO 支付参数
     */
    String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO);

}
