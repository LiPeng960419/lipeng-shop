package com.lipeng.portalpay.feign;

import com.lipeng.pay.service.PayContextService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-pay")
public interface PayContextFeign extends PayContextService {

}
