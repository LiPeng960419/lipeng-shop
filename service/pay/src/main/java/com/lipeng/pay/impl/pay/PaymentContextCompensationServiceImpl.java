package com.lipeng.pay.impl.pay;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.base.BaseApiService;
import com.lipeng.base.BaseResponse;
import com.lipeng.pay.compensation.strategy.PaymentCompensationStrategy;
import com.lipeng.pay.compensation.strategy.factory.CompensationStrategyFactory;
import com.lipeng.pay.mapper.PaymentChannelMapper;
import com.lipeng.pay.mapper.PaymentTransactionMapper;
import com.lipeng.pay.mapper.entity.PaymentChannelEntity;
import com.lipeng.pay.mapper.entity.PaymentTransactionEntity;
import com.lipeng.pay.service.pay.PaymentContextCompensationService;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentContextCompensationServiceImpl extends BaseApiService<JSONObject>
        implements PaymentContextCompensationService {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Override
    public BaseResponse<JSONObject> payMentCompensation(String payMentId) {
        if (StringUtils.isEmpty(payMentId)) {
            return setResultError("payMentId不能为空");
        }
        PaymentTransactionEntity paymentTransaction = paymentTransactionMapper.selectByPaymentId(payMentId);
        if (paymentTransaction == null) {
            return setResultError("paymentTransaction为空!");
        }

        // 2.获取所有的渠道重试id
        List<PaymentChannelEntity> paymentChannelList = paymentChannelMapper.selectAll();
        for (PaymentChannelEntity pcl : paymentChannelList) {
            if (pcl != null) {
                return compensationStrategy(paymentTransaction, pcl);
            }
        }

        return setResultError("没有执行重试任务");
    }

    private BaseResponse<JSONObject> compensationStrategy(PaymentTransactionEntity paymentTransaction,
            PaymentChannelEntity paymentChannelEntity) {
        String retryBeanId = paymentChannelEntity.getRetryBeanId();
        PaymentCompensationStrategy paymentCompensationStrategy = CompensationStrategyFactory
                .getPaymentCompensationStrategy(retryBeanId);
        // 3.实现子类重试
        boolean payMentCompensation = paymentCompensationStrategy
                .payMentCompensation(paymentTransaction, paymentChannelEntity);
        return payMentCompensation ? setResultSuccess("重试成功!") : setResultError("重试失败!");
    }

}
