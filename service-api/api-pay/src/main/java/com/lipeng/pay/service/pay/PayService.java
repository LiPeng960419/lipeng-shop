package com.lipeng.pay.service.pay;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:13
 */
@Controller
@RequestMapping("/pay")
public interface PayService {

    @GetMapping("/queryPayment")
    BaseResponse<JSONObject> queryF2F(@RequestParam("paymentId") String paymentId);

    @GetMapping("/refund")
    BaseResponse<JSONObject> refund(@RequestParam("id") Long id);

    @GetMapping("/refund/query")
    BaseResponse<JSONObject> refundQuery(@RequestParam("id") Long id);

    @GetMapping("/cancel")
    BaseResponse<JSONObject> cancel(@RequestParam("id") Long id);

    @GetMapping("/close")
    BaseResponse<JSONObject> close(@RequestParam("id") Long id);

    @GetMapping("/settle")
    BaseResponse<JSONObject> settle(@RequestParam("id") Long id);

}