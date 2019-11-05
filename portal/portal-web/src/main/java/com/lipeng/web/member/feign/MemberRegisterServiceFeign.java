package com.lipeng.web.member.feign;

import com.lipeng.member.service.MemberRegisterService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-member")
public interface MemberRegisterServiceFeign extends MemberRegisterService {

}
