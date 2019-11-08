package com.lipeng.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/11/8 11:55
 */
@Component
@ConfigurationProperties(prefix = "qq")
@Data
public class QQConfig {
    private String appid;
    private String appkey;
    private String reDirectUri;
    private String defaultPicUrl;//用户头像默认地址
}