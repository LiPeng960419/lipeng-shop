package com.lipeng.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PaymentContextCompensationService {

    /**
     * 根据payMentId查询支付信息
     */
    @GetMapping("/payMentCompensation")
    BaseResponse<JSONObject> payMentCompensation(@RequestParam("payMentId") String payMentId);

}