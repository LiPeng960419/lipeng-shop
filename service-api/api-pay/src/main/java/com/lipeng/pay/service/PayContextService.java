package com.lipeng.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PayContextService {

    @GetMapping("/toPayHtml")
    BaseResponse<JSONObject> toPayHtml(@RequestParam("channelId") String channelId,
            @RequestParam("payToken") String payToken);

}