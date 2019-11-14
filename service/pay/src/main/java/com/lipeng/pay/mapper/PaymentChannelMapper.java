package com.lipeng.pay.mapper;

import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import java.util.List;
import org.apache.ibatis.annotations.Select;

public interface PaymentChannelMapper {

    @Select("SELECT CHANNEL_NAME AS channelName, CHANNEL_ID AS channelId, MERCHANT_ID AS merchantId, SYNC_URL AS syncUrl, ASYN_URL AS asynUrl, PUBLIC_KEY AS publicKey, PRIVATE_KEY AS privateKey, CHANNEL_STATE AS channelState ,CLASS_ADDRESS as classAddress FROM payment_channel WHERE CHANNEL_STATE = 0;")
    List<PaymentChannelEntity> selectAll();

    @Select("SELECT CHANNEL_NAME AS channelName, CHANNEL_ID AS channelId, MERCHANT_ID AS merchantId, SYNC_URL AS syncUrl, ASYN_URL AS asynUrl, PUBLIC_KEY AS publicKey, PRIVATE_KEY AS privateKey, CHANNEL_STATE AS channelState ,CLASS_ADDRESS as classAddress FROM payment_channel WHERE CHANNEL_STATE = 0 AND CHANNEL_ID = #{channelId};")
    PaymentChannelEntity selectBychannelId(String channelId);

}