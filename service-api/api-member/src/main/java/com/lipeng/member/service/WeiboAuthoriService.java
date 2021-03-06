package com.lipeng.member.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface WeiboAuthoriService {

    /**
     * 根据 openid查询是否已经绑定,如果已经绑定，则直接实现自动登陆
     */
    @RequestMapping("/weibo/findByOpenId")
    BaseResponse<JSONObject> findByOpenId(@RequestParam("weiboOpenId") String weiboOpenId);

}