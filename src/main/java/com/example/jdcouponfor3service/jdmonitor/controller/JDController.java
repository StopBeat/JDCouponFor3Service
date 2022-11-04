package com.example.jdcouponfor3service.jdmonitor.controller;


import com.example.jdcouponfor3service.jdmonitor.application.dto.JDCouponsCookieDTO;
import com.example.jdcouponfor3service.jdmonitor.application.service.JD.JDCouponsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/JDCoupons")
@Api(value = "京东接口", tags = {"京东接口"})
public class JDController {
    @Autowired
    private JDCouponsService jdCouponsService;

    @ApiOperation("京东自动领券信息修改")
    @PostMapping("/changeCookieInfo")
    public void changeCookieInfo(@RequestBody JDCouponsCookieDTO dto){
        jdCouponsService.changeCookieInfo(dto);
    }

    @ApiOperation("京东新增自动领券")
    @PostMapping("/insertCookie")
    public void insertCookie(@RequestBody JDCouponsCookieDTO dto){
        jdCouponsService.insertCookie(dto);
    }
}
