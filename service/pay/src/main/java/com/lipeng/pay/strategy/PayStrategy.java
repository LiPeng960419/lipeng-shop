package com.lipeng.pay.strategy;

import com.lipeng.pay.dto.PayMentTransacDTO;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;

public interface PayStrategy {

    String ALI_PAY_CHANNEL_ID = "alipay";

    String UNION_PAY_CHANNEL_ID = "yinlian_pay";

    String ALI_MOBILE_PAY_CHANNEL_ID = "ali_mobile_pay";

    String ALI_F2F_PAY_CHANNEL_ID = "ali_f2f_pay";

    String ALI_APP_PAY_CHANNEL_ID = "ali_app_pay";

    /**
     * @param pymentChannel 渠道参数
     * @param payMentTransacDTO 支付参数
     */
    String toPayHtml(PaymentChannelEntity pymentChannel, PayMentTransacDTO payMentTransacDTO);

}
