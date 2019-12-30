package com.lipeng.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 15:03
 */
@Controller
public interface AlipayF2FService {

    /*
    适用场景：商家获取二维码展示在屏幕上，然后用户去扫描屏幕上的二维码
     */
    @GetMapping("/precreate")
    BaseResponse<JSONObject> precreate(@RequestParam("payToken") String payToken);

}