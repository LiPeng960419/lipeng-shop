package com.lipeng.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weibo")
@Data
public class WeiBoConfig {

    private String appkey;
    private String secret;
    private String reDirectUri;
}
