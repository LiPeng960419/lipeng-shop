package com.lipeng.pay.utils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.lipeng.alipay.config.AlipayConfig;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:32
 */
public class AliPayUtil {

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    private AliPayUtil() {
    }

    /**
     * 将分为单位的转换为元 （除100）
     */
//    public static String changeF2Y(String amount) {
//        if (!amount.matches(CURRENCY_FEN_REGEX)) {
//            return null;
//        }
//        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
//    }

    public static String changeF2Y(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return null;
        }
        return new BigDecimal(amount).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP)
                .toString();
    }

    public static AlipayClient getAlipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        return alipayClient;
    }

    public static Map<String, String> verifySignature(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

}