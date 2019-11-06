package com.lipeng.web.member.feign;

import com.lipeng.member.service.QQAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-member")
public interface QQAuthoriFeign extends QQAuthoriService {

}