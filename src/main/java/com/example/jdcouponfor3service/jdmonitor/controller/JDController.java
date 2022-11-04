package com.example.jdcouponforotherservice.jdmonitor.controller;


import com.example.jdcouponforotherservice.jdmonitor.application.dto.JDCouponsCookieDTO;
import com.example.jdcouponforotherservice.jdmonitor.application.dto.JDCouponsDTO;
import com.example.jdcouponforotherservice.jdmonitor.application.dto.JDMonitorDTO;
import com.example.jdcouponforotherservice.jdmonitor.application.service.JD.JDCouponsService;
import com.example.jdcouponforotherservice.jdmonitor.application.service.JD.JDMonitorService;
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
    @Autowired
    private JDMonitorService jdMonitorService;
    @ApiOperation("京东抢券")
    @PostMapping("/buyCoupons")
    public String buyCoupons(@RequestBody JDCouponsDTO dto){
        jdCouponsService.buyCoupons(dto);
        return "正在执行任务,请关注控制台消息";
    }
    @ApiOperation("京东监控")
    @PostMapping("/monitor")
    public String monitor(@RequestBody JDMonitorDTO dto){
        jdMonitorService.startMonitorTask(dto);
        return "正在执行监控,请关注控制台消息";
    }
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
