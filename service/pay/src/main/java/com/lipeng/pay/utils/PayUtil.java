package com.lipeng.pay.utils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.lipeng.alipay.config.AlipayConfig;
import java.math.BigDecimal;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:32
 */
public class PayUtil {

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    private PayUtil() {
    }

    /**
     * 将分为单位的转换为元 （除100）
     */
    public static String changeF2Y(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return null;
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    public static AlipayClient getAlipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        return alipayClient;
    }

}