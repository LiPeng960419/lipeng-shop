package com.lipeng.pay.mapper;

import com.lipeng.pay.mapper.entity.PaymentTransactionLogEntity;
import org.apache.ibatis.annotations.Insert;

public interface PaymentTransactionLogMapper {

    @Insert("INSERT INTO payment_transaction_log VALUES (null, null, #{asyncLog}, null, #{transactionId}, null, null, NOW(), null, NOW(), null);")
    int insertTransactionLog(PaymentTransactionLogEntity paymentTransactionLog);

}