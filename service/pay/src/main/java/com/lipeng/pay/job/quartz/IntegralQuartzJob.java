package com.lipeng.pay.job.quartz;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.constants.Constants;
import com.lipeng.pay.mapper.BrokerMessageLogMapper;
import com.lipeng.pay.mapper.entity.BrokerMessageLog;
import com.lipeng.pay.mq.producer.IntegralProducer;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Author: lipeng 910138
 * @Date: 2020/1/4 0:17
 */
@Slf4j
public class IntegralQuartzJob extends QuartzJobBean {

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Autowired
    private IntegralProducer producer;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("-----RABBITMQ积分消息重传定时任务开始-----");
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTryCount() >= 3) {
                brokerMessageLogMapper.changeBrokerMessageLogStatus(list.get(i).getMessageId(), Constants.SEND_FAILURE, new Date());
            } else {
                brokerMessageLogMapper.update4ReSend(list.get(i).getMessageId(), new Date());
                log.info("积分消息重传第" + brokerMessageLogMapper.queryBrokerMessageById(list.get(i).getMessageId()).getTryCount() + "次");
                JSONObject object = JSONObject.parseObject(list.get(i).getMessage());
                try {
                    producer.send(object);
                } catch (Exception e) {
                    log.error("-----RABBITMQ积分消息重传定时任务异常-----", e);
                }
            }
        }
        log.info("-----RABBITMQ积分消息重传定时任务结束-----");
    }

}