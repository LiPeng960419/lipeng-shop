package com.lipeng.portalpay.feign;

import com.lipeng.pay.service.PaymentChannelService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-pay")
public interface PaymentChannelFeign extends PaymentChannelService {

}