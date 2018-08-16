package com.springboot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@SpringBootApplication
@DubboComponentScan("com.springboot")
@EnableTransactionManagement
public class engin {
    public static void main(String[] args) {
        SpringApplication.run(engin.class, args);
    }
}
