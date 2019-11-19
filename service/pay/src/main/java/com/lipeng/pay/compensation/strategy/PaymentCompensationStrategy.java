package com.lipeng.pay.compensation.strategy;

import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;

public interface PaymentCompensationStrategy {

    boolean payMentCompensation(PaymentTransactionEntity paymentTransaction,
            PaymentChannelEntity paymentChanne);

}