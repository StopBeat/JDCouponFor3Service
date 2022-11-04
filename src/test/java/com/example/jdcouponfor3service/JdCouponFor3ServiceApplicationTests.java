package com.example.jdcouponfor3service;

import com.example.jdcouponfor3service.jdmonitor.application.service.JD.JDCouponsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdCouponFor3ServiceApplicationTests {
    @Autowired
    JDCouponsService jdCouponsService;
    @Test
    void contextLoads() {
    }

}
