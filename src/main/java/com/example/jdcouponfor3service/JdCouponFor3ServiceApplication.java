package com.example.jdcouponfor3service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class JdCouponFor3ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdCouponFor3ServiceApplication.class, args);
    }

}
