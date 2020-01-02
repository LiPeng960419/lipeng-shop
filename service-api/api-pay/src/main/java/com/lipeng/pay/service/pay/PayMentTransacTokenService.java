package com.lipeng.pay.service.pay;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseResponse;
import com.lipeng.pay.dto.PayCratePayTokenDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

public interface PayMentTransacTokenService {

    /**
     * 创建支付令牌
     */
    @GetMapping("/createPayToken")
    BaseResponse<JSONObject> createPayToken(@Validated PayCratePayTokenDto payCratePayTokenDto);

}
