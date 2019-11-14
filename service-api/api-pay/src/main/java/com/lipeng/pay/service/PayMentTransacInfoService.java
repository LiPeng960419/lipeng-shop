package com.lipeng.pay.service;

import com.lipeng.base.BaseResponse;
import com.lipeng.pay.dto.PayMentTransacDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PayMentTransacInfoService {

    @GetMapping("/tokenByPayMentTransac")
    BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(@RequestParam("token") String token);

}