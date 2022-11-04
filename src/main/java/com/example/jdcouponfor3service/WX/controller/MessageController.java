package com.example.jdcouponfor3service.WX.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.jdcouponfor3service.WX.application.service.AnalysisURL;
import com.example.jdcouponfor3service.jdmonitor.application.service.JD.JDCouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/WXMessage")
public class MessageController {
    @Autowired
    JDCouponsService jdCouponsService;
    @Autowired
    AnalysisURL analysisURL;

    @RequestMapping("/sendApi")
    public JSONObject getMessage(@RequestParam("from_wxid") String from_wxid, @RequestParam("msg") String msg) {
        JSONObject res = new JSONObject();
        try {
            if (from_wxid.equals("44845885969@chatroom") || from_wxid.equals("wxid_zqho0mzddqh222")) {
                if (msg.contains("key") && msg.contains("roleId")) {
                    jdCouponsService.getCouponsByKeyWay2(msg, false);
                }
                if (msg.contains("?linkKey")) {
                    jdCouponsService.getCouponsByLinkKeyWay2(msg, false);
                }
                if(msg.contains("https://u.jd.com/")){
                    List<String> list = analysisURL.analysisURL(msg);
                    for(String url:list){
                        jdCouponsService.getCouponsFromFanLi(url);
                    }
                }
            }
            if (from_wxid.equals("47367853481@chatroom")) {
                if (msg.contains("key") && msg.contains("roleId")) {
                    jdCouponsService.getCouponsByKeyWay2(msg, true);
                }
                if (msg.contains("?linkKey")) {
                    jdCouponsService.getCouponsByLinkKeyWay2(msg, true);
                }
                if(msg.contains("https://u.jd.com/")){
                    List<String> list = analysisURL.analysisURL(msg);
                    for(String url:list){
                        jdCouponsService.getCouponsFromFanLi(url);
                    }
                }
            }
        }catch (Exception e){
            res.put("msg","fail");
            return res;
        }
        res.put("msg","success");
        return res;
    }

}
