package com.lipeng.web.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/6 16:14
 */
//maxInactiveIntervalInSeconds session过期时间 15min
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 900)
@Configuration
public class SessionConfig {

}