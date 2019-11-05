package com.lipeng.member.feign;

import com.lipeng.service.VerificaCodeService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-weixin")
public interface VerificaCodeServiceFeign extends VerificaCodeService {

}
