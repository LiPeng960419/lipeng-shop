package com.lipeng.portalpay.feign;

import com.lipeng.pay.service.pay.PayMentTransacInfoService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-pay")
public interface PayMentTransacInfoFeign extends PayMentTransacInfoService {

}
