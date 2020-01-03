package com.lipeng.pay.common.config;

import com.lipeng.pay.worker.impl.CachedUidGenerator;
import com.lipeng.pay.worker.impl.DisposableWorkerIdAssigner;
import com.lipeng.pay.worker.impl.UidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lipeng 910138
 * @Date: 2020/1/2 14:05
 */
@Configuration
public class IdGeneratorConfiguration {

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public UidGenerator cachedUidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setTimeBits(29);
        cachedUidGenerator.setWorkerBits(21);
        cachedUidGenerator.setSeqBits(13);
        cachedUidGenerator.setEpochStr("2020-01-01");
        cachedUidGenerator.setBoostPower(3);
        cachedUidGenerator.setScheduleInterval(60);
        cachedUidGenerator.setPaddingFactor(50);
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return cachedUidGenerator;
    }

}