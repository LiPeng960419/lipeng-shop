package com.lipeng.spike.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.core.token.GenerateToken;
import com.lipeng.spike.config.RateLimitAspect;
import com.lipeng.spike.producer.SpikeCommodityProducer;
import com.lipeng.spike.service.SpikeCommodityService;
import com.lipeng.spike.service.mapper.OrderMapper;
import com.lipeng.spike.service.mapper.SeckillMapper;
import com.lipeng.spike.service.mapper.entity.OrderEntity;
import com.lipeng.spike.service.mapper.entity.SeckillEntity;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SpikeCommodityServiceImpl extends BaseApiService<JSONObject> implements
        SpikeCommodityService {

    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private SpikeCommodityProducer spikeCommodityProducer;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    @HystrixCommand(fallbackMethod = "spikeFallback")
    @RateLimitAspect()
    public BaseResponse<JSONObject> spike(String phone, Long seckillId) {
        log.info(">>>>>>秒杀服务接口:spike()线程名称:" + Thread.currentThread().getName());
        // 1.参数验证
        if (StringUtils.isEmpty(phone)) {
            return setResultError("手机号码不能为空!");
        }
        if (seckillId == null) {
            return setResultError("商品库存id不能为空!");
        }
        OrderEntity orderEntity = orderMapper.findByOrder(phone, seckillId);
        if (Objects.nonNull(orderEntity)) {
            return setResultError("不能重复秒杀噢亲!");
        }
        // 2.获取到秒杀token  zuul里面有责任链可以实现
        String seckillToken = generateToken.getListKeyToken(String.valueOf(seckillId));
        if (StringUtils.isEmpty(seckillToken)) {
            log.info(">>>seckillId:{}, 亲，该秒杀已经售空，请下次再来!", seckillId);
            return setResultSuccess("亲，该秒杀已经售空，请下次再来!");
        }
        // 3.获取到秒杀token之后，异步放入mq中实现修改商品的库存
        sendSeckillMsg(seckillId, phone);
        return setResultSuccess("正在排队中.......");
    }

    private BaseResponse<JSONObject> spikeFallback(String phone, Long seckillId) {
        return setResultError("服务器忙,请稍后重试!");
    }

    /**
     * 获取到秒杀token之后，异步放入mq中实现修改商品的库存
     */
    public void sendSeckillMsg(Long seckillId, String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillId);
        jsonObject.put("phone", phone);
        spikeCommodityProducer.send(jsonObject);
    }

    // 采用redis数据库类型为 list类型 key为 商品库存id list 多个秒杀token
    @Override
    public BaseResponse<JSONObject> addSpikeToken(Long seckillId, Long tokenQuantity) {
        // 1.验证参数
        if (seckillId == null) {
            return setResultError("商品库存id不能为空!");
        }
        if (tokenQuantity == null) {
            return setResultError("token数量不能为空!");
        }
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            return setResultError("商品信息不存在!");
        }
        // 2.使用多线程异步生产令牌
        createSeckillToken(seckillId, tokenQuantity);
        return setResultSuccess("令牌正在生成中.....");
    }

    public void createSeckillToken(Long seckillId, Long tokenQuantity) {
        generateToken.createListToken("seckill_", seckillId + "", tokenQuantity);
    }

}