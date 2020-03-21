package com.lipeng.web.member.feign;

import com.lipeng.member.service.WeiXinAuthoriService;
import com.lipeng.member.service.WeiboAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("app-member")
public interface WeiBoAuthoriFeign extends WeiboAuthoriService {

}
