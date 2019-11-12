package com.lipeng.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.lipeng")
@EnableEurekaClient
@EnableFeignClients
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

}
