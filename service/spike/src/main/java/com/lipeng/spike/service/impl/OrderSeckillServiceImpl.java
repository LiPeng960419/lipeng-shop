package com.lipeng.spike.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.spike.service.OrderSeckillService;
import com.lipeng.spike.service.mapper.OrderMapper;
import com.lipeng.spike.service.mapper.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderSeckillServiceImpl extends BaseApiService<JSONObject> implements
        OrderSeckillService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public BaseResponse<JSONObject> getOrder(String phone, Long seckillId) {
        log.info(">>>>>>查询秒杀结果线程名称:" + Thread.currentThread().getName());
        if (StringUtils.isEmpty(phone)) {
            return setResultError("手机号码不能为空!");
        }
        if (seckillId == null) {
            return setResultError("商品库存id不能为空!");
        }
        OrderEntity orderEntity = orderMapper.findByOrder(phone, seckillId);
        if (orderEntity == null) {
            return setResultError("正在排队中.....");
        }
        return setResultSuccess("恭喜你秒杀成功!");
    }

}
