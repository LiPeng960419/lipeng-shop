package com.lipeng.pay.utils;

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

    /**
     * 将分为单位的转换为元 （除100）
     */
    public static String changeF2Y(String amount) {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            return null;
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

}
