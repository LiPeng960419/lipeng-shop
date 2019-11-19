package com.lipeng.integral.mapper;

import com.lipeng.integral.mapper.entity.IntegralEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface IntegralMapper {

    @Insert("INSERT INTO meite_integral VALUES (NULL, #{userId}, #{paymentId},#{integral}, #{availability}, 0, null, now(), null, now());")
    int insertIntegral(IntegralEntity eiteIntegralEntity);

    @Select("SELECT ID as id ,USER_ID as userId, PAYMENT_ID as paymentId ,INTEGRAL as integral ,AVAILABILITY as availability FROM meite_integral where PAYMENT_ID = #{paymentId}  AND AVAILABILITY = 1;")
    IntegralEntity findIntegral(String paymentId);

}