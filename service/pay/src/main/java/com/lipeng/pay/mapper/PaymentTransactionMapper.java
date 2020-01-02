package com.lipeng.pay.mapper;

import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PaymentTransactionMapper {

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO payment_transaction VALUES (null, #{payAmount}, 0, #{userId}, #{orderId}, null, null, now(), null, now(),null,#{paymentId},null);")
    int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

    @Select("SELECT ID AS ID, PAY_AMOUNT AS payAmount, PAYMENT_STATUS AS paymentStatus, USER_ID AS userId, ORDER_ID AS orderId, CREATED_TIME as createdTime, PARTY_PAYID as partyPayId, PAYMENT_ID as paymentId, PAYMENT_CHANNEL as paymentChannel FROM payment_transaction WHERE ID = #{id};")
    PaymentTransactionEntity selectById(Long id);

    @Select("SELECT ID AS ID, PAY_AMOUNT AS payAmount, PAYMENT_STATUS AS paymentStatus, USER_ID AS userId, ORDER_ID AS orderId, CREATED_TIME as createdTime, PARTY_PAYID as partyPayId, PAYMENT_ID as paymentId, PAYMENT_CHANNEL as paymentChannel FROM payment_transaction WHERE PAYMENT_ID = #{paymentId};")
    PaymentTransactionEntity selectByPaymentId(String paymentId);

    int updatePaymentStatus(@Param("paymentStatus") String paymentStatus,
            @Param("tradeNo") String tradeNo,
            @Param("paymentId") String paymentId, @Param("paymentChannel") String paymentChannel);

    int updatePaymentStatusByPaymentId(@Param("paymentStatus") String paymentStatus, @Param("paymentId") String paymentId);

}