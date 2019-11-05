package com.lipeng.web.member.feign;

import com.lipeng.member.service.MemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-member")
public interface MemberLoginServiceFeign extends MemberLoginService {

}
