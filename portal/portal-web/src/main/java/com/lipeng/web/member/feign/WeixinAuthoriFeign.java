package com.lipeng.web.member.feign;

import com.lipeng.member.service.WeiXinAuthoriService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/7 17:04
 */
@FeignClient("app-member")
public interface WeixinAuthoriFeign extends WeiXinAuthoriService {

}
