package com.lipeng.pay.impl.pay;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.pay.service.pay.UnionPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/30 16:16
 */
@Slf4j
@RestController
public class UnionPayServiceImpl extends BaseApiService<JSONObject>
        implements UnionPayService {

    @Override
    public BaseResponse<JSONObject> queryPayment(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> refund(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> refundQuery(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> cancel(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> close(Long id) {
        return null;
    }

    @Override
    public BaseResponse<JSONObject> settle(Long id) {
        return null;
    }

}