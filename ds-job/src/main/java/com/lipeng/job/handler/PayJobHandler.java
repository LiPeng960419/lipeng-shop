package com.lipeng.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@JobHandler(value = "payJobHandler")
@Component
@Slf4j
public class PayJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info(">>>使用任务调度实现自动化对账");
        return SUCCESS;
    }

}