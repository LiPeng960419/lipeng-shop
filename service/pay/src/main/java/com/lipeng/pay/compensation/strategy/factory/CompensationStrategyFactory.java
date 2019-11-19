package com.lipeng.pay.compensation.strategy.factory;

import com.lipeng.pay.compensation.strategy.PaymentCompensationStrategy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/19 17:43
 */
@Slf4j
public class CompensationStrategyFactory {

    private static Map<String, PaymentCompensationStrategy> strategyBean = new ConcurrentHashMap<String, PaymentCompensationStrategy>();

    public static PaymentCompensationStrategy getPaymentCompensationStrategy(String retryBeanId) {
        try {
            if (StringUtils.isEmpty(retryBeanId)) {
                return null;
            }
            PaymentCompensationStrategy paymentCompensationStrategy = strategyBean.get(retryBeanId);
            if (paymentCompensationStrategy != null) {
                return paymentCompensationStrategy;
            }
            // 1.使用Java的反射机制初始化子类
            Class<?> forName = Class.forName(retryBeanId);
            // 2.反射机制初始化对象
            PaymentCompensationStrategy compensationStrategy = (PaymentCompensationStrategy) forName.newInstance();
            strategyBean.put(retryBeanId, compensationStrategy);
            return compensationStrategy;
        } catch (Exception e) {
            log.error("getPayStrategy error", e);
            return null;
        }
    }

}