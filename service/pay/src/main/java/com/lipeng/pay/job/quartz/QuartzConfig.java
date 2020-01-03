package com.lipeng.pay.job.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lipeng 910138
 * @Date: 2019/8/12 11:19
 */
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail mapCacheTask() {
        return JobBuilder.newJob(IntegralQuartzJob.class).withIdentity("IntegralQuartzJob")
                .requestRecovery()
                .storeDurably().build();
    }

    @Bean
    public Trigger mapCacheTrigger() {
        return TriggerBuilder.newTrigger().forJob(mapCacheTask()).withIdentity("IntegralQuartzJob")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")).build();
    }

}