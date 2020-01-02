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
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        return cachedUidGenerator;
    }

}