package com.xxl.sso.server.feign;

import com.lipeng.member.service.MemberService;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("app-member")
public interface MemberServiceFeign extends MemberService {

}
