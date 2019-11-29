package com.lipeng.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lipeng")
public class DsJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsJobApplication.class, args);
    }

}