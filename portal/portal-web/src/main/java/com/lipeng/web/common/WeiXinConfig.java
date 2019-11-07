package com.lipeng.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/7 15:44
 */
@Component
@ConfigurationProperties(prefix = "wechat")
@Data
public class WeiXinConfig {

    private String appid;
    private String secret;
}
