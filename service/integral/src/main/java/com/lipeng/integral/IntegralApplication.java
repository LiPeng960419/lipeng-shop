package com.lipeng.integral;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lipeng")
@MapperScan(basePackages = "com.lipeng.integral.mapper")
public class IntegralApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegralApplication.class, args);
    }

}