package com.lipeng.spike.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;

public interface OrderSeckillService {

    @RequestMapping("/getOrder")
    BaseResponse<JSONObject> getOrder(String phone, Long seckillId);

}