package com.lipeng.zuul.feign;

import com.lipeng.auth.service.AuthorizationService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("app-auth")
public interface AuthorizationServiceFeign extends AuthorizationService {

}