package com.huiyu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Gateway8000 {
    public static void main(String[] args) {
        SpringApplication.run(Gateway8000.class, args);
    }
}
