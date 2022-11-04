package com.example.jdcouponforotherservice.jdmonitor.application.service.JD;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.example.jdcouponforotherservice.jdmonitor.application.dto.JDCouponsCookieDTO;
import com.example.jdcouponforotherservice.jdmonitor.application.dto.JDCouponsDTO;
import com.example.jdcouponforotherservice.jdmonitor.application.task.JDCouponsTask;
import com.example.jdcouponforotherservice.jdmonitor.infrastructure.dataobject.JdCouponsCookieSecond;
import com.example.jdcouponforotherservice.jdmonitor.infrastructure.repository.JdCouponsCookieRepository;
import com.example.jdcouponforotherservice.utils.HttpUtil;
import com.example.jdcouponforotherservice.utils.WXMessageUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Log4j2
public class JDCouponsService {
    @Autowired
    private JDCouponsTask task;
    @Autowired
    JdCouponsCookieRepository repository;
    @Autowired
    HttpUtil httpUtil;

    @Autowired
    WXMessageUtil wxMessageUtil;

    private String MY_FANG_TANG = "SCT110265TJ530QlLf2CCvTSIn9T7XBaho";

    public void buyCoupons(JDCouponsDTO dto) {
        task.buyCouponsTask(dto);
    }

    public void getCouponsByKeyWay1(String url, boolean onlyUs) {
        try {
            List<String> keyList = parseData(url, "key方式1");
            List<String> roleIdList = parseData(url, "roleId方式1");
            for (int i = 0; i < Math.min(keyList.size(), roleIdList.size()); i++) {
                String detail = getCouponsDetail(keyList.get(i), roleIdList.get(i));
                Iterable<JdCouponsCookieSecond> all = repository.findAll();
                String trueUrl = "https://s.m.jd.com/activemcenter/mfreecoupon/getcoupon?key=" + keyList.get(i) + "&roleId=" + roleIdList.get(i);
                for (JdCouponsCookieSecond cookieEntity : all) {
                    if (onlyUs) {
                        if (cookieEntity.getOnlyus().equals("是")) {
                            boolean error = sendRequest(cookieEntity, trueUrl, detail);
                            if(!error){
                                break;
                            }
                        }
                    } else {
                        boolean error = sendRequest(cookieEntity, trueUrl, detail);
                        if(!error){
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            wxMessageUtil.sendZhiXiMessage(MY_FANG_TANG, "领券出错", url);
        }
    }

    public void getCouponsByKeyWay2(String url, boolean onlyUs) {
        try {
            List<String> keyList = parseData(url, "key方式2");
            List<String> roleIdList = parseData(url, "roleId方式2");
            for (int i = 0; i < Math.min(keyList.size(), roleIdList.size()); i++) {
                String detail = getCouponsDetail(keyList.get(i), roleIdList.get(i));
                Iterable<JdCouponsCookieSecond> all = repository.findAll();
                String trueUrl = "https://s.m.jd.com/activemcenter/mfreecoupon/getcoupon?key=" + keyList.get(i) + "&roleId=" + roleIdList.get(i);
                for (JdCouponsCookieSecond cookieEntity : all) {
                    if (onlyUs) {
                        if (cookieEntity.getOnlyus().equals("是")) {
                            boolean error = sendRequest(cookieEntity, trueUrl, detail);
                            if(!error){
                                break;
                            }
                        }
                    } else {
                        boolean error = sendRequest(cookieEntity, trueUrl, detail);
                        if(!error){
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            getCouponsByKeyWay1(url, onlyUs);
        }
    }

    public void getCouponsByLinkKeyWay1(String url, boolean onlyUs) {
        try {
            List<String> keyList = parseData(url, "linkKey方式1");
            for (int i = 0; i < keyList.size(); i++) {
                String detail = getCouponsDetail2(keyList.get(i));
                Iterable<JdCouponsCookieSecond> all = repository.findAll();
                String trueUrl = "https://s.m.jd.com/activemcenter/mfreecoupon/getcoupon?linkKey=" + keyList.get(i);
                for (JdCouponsCookieSecond cookieEntity : all) {
                    if (onlyUs) {
                        if (cookieEntity.getOnlyus().equals("是")) {
                            boolean error = sendRequest(cookieEntity, trueUrl, detail);
                            if(!error){
                                break;
                            }
                        }
                    } else {
                        boolean error = sendRequest(cookieEntity, trueUrl, detail);
                        if(!error){
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            wxMessageUtil.sendZhiXiMessage(MY_FANG_TANG, "领券出错", url);
        }
    }

    public void getCouponsByLinkKeyWay2(String url, boolean onlyUs) {
        try {
            List<String> keyList = parseData(url, "linkKey方式2");
            for (int i = 0; i < keyList.size(); i++) {
                String detail = getCouponsDetail2(keyList.get(i));
                Iterable<JdCouponsCookieSecond> all = repository.findAll();
                String trueUrl = "https://s.m.jd.com/activemcenter/mfreecoupon/getcoupon?linkKey=" + keyList.get(i);
                for (JdCouponsCookieSecond cookieEntity : all) {
                    if (onlyUs) {
                        if (cookieEntity.getOnlyus().equals("是")) {
                            boolean error = sendRequest(cookieEntity, trueUrl, detail);
                            if(!error){
                               break;
                            }
                        }
                    } else {
                        boolean error = sendRequest(cookieEntity, trueUrl, detail);
                        if(!error){
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            getCouponsByLinkKeyWay1(url, onlyUs);
        }
    }

    public boolean sendRequest(JdCouponsCookieSecond cookieEntity, String trueUrl, String detail) throws Exception {
        try {
            String wxPush = cookieEntity.getWxpush();
            String cookie = cookieEntity.getCookies();
            String needSend = cookieEntity.getNeedsuccesspush();
            int day = cookieEntity.getUsedate();
            if (day == 0) {
                wxMessageUtil.sendZhiXiMessage(wxPush, "账号" + cookieEntity.getPhone() + "自动领券使用时间过期，请联系作者充值", "自动领券使用时间过期，请联系作者充值");
                return true;
            }
            String header = "{" +
                    "  \"cache-control\": \"no-cache\",\n" +
                    "  \"cookie\": \"" + cookie + "\",\n" +
                    "  \"referer\": \"https://coupon.m.jd.com/\",\n" +
                    "  \"user-agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\"\n" +
                    "}";
            String res = httpUtil.getRequestFromHTML(trueUrl, header, false);
            res = res.replace("\n", "");
            String pattern = "(\\(.*?\\))";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(res);
            boolean b = m.find();
            if (b) {
                res = m.group(0).substring(1, m.group(0).length() - 1);
                JSONObject object = JSON.parseObject(res);
                if (object.getInteger("ret") == 2) {
                    try {
                        if (object.getString("errmsg").equals("您还未登录")) {
                            wxMessageUtil.sendZhiXiMessage(wxPush, "京东Cookies过期，请及时更换", object.toJSONString());
                        }
                    } catch (Exception e) {
                        wxMessageUtil.sendZhiXiMessage(wxPush, object.toJSONString(), object.toJSONString());
                    }
                }
                if (object.getInteger("ret") == 999) {
                    log.info("账号{}成功领取优惠券！", cookieEntity.getPhone());
                    if (needSend.equals("是")) {
                        wxMessageUtil.sendZhiXiMessage(wxPush, "账号" + cookieEntity.getPhone() + "成功领取优惠券！", detail);
                    }
                } else if (object.getInteger("ret") == 93) {
                    log.info("账号{}未加入会员！", cookieEntity.getPhone());
                    wxMessageUtil.sendZhiXiMessage(wxPush, "账号" + cookieEntity.getPhone() + "未加入会员！", detail);
                } else if (object.getInteger("ret") == 1) {
                    log.info("解析出错");
                    throw new Exception();
                } else if (object.getInteger("ret") == 17) {
                    log.info("优惠券已经被抢完了");
                    return false;
                } else if (object.getInteger("ret") == 6) {
                    log.info("活动已结束或不存在");
                    return false;
                } else if (object.getInteger("ret") == 39) {
                    log.info("手机号{}：啊哦，优惠券与您擦肩而过了哟，看看其他活动吧", cookieEntity.getPhone());
                    log.info("trueUrl = " + trueUrl);
                } else if (object.getInteger("ret") == 145) {
                    sendRequest(cookieEntity, trueUrl, detail);
                } else if (object.getInteger("ret") == 14) {
                    log.info("账号{}已经领取过优惠券了", cookieEntity.getPhone());
                } else if (object.getInteger("ret") == 16){
                    log.info("本时段优惠券已被抢完");
                    return false;
                }
                else if (object.getInteger("ret") != 15 && object.getInteger("ret") != -1) {
                    wxMessageUtil.sendZhiXiMessage(wxPush, "账号" + cookieEntity.getPhone() + "领券出错！", detail + object.toJSONString());
                }
            }
        } catch (Exception e) {
            wxMessageUtil.sendFangTangMessage(MY_FANG_TANG,"发生错误",trueUrl);
            return false;
        }
        return true;
    }

    public String getCouponsDetail(String key, String roleId) {
        try {
            String url = "https://coupon.m.jd.com/coupons/show.action?key=" + key + "&roleId=" + roleId + "&to=";
            String header = "{" +
                    "  \"referer\": \"https://coupon.m.jd.com/\",\n" +
                    "  \"user-agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\"\n" +
                    "}\n";
            String res = httpUtil.getRequestFromHTML(url, header, false);
            String p = "limitStr\":\"(.*?)\",";
            Pattern r = Pattern.compile(p);
            Matcher m = r.matcher(res);
            String title = "";
            String notes = "";
            if (m.find()) {
                title = m.group(0).substring(11, m.group(0).length() - 2);
            }
            String p2 = "notes\":\"(.*?)\",";
            Pattern r2 = Pattern.compile(p2);
            Matcher m2 = r2.matcher(res);
            if (m2.find()) {
                notes = m2.group(0).substring(8, m2.group(0).length() - 2);
            }
            return notes + title + "  原链接为:" + url;
        } catch (Exception e) {
            log.info(e);
            return "";
        }
    }

    public String getCouponsDetail2(String linkKey) {
        try {
            String url = "https://coupon.m.jd.com/coupons/show.action?linkKey=" + linkKey;
            String header = "{" +
                    "  \"referer\": \"https://coupon.m.jd.com/\",\n" +
                    "  \"user-agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\"\n" +
                    "}\n";
            String res = httpUtil.getRequestFromHTML(url, header, false);
            String p = "limitStr\":\"(.*?)\",";
            Pattern r = Pattern.compile(p);
            Matcher m = r.matcher(res);
            String title = "";
            String notes = "";
            if (m.find()) {
                title = m.group(0).substring(11, m.group(0).length() - 2);
            }
            String p2 = "notes\":\"(.*?)\",";
            Pattern r2 = Pattern.compile(p2);
            Matcher m2 = r2.matcher(res);
            if (m2.find()) {
                notes = m2.group(0).substring(8, m2.group(0).length() - 2);
            }
            return notes + title + "  原链接为:" + url;
        } catch (Exception e) {
            log.info(e);
            return "";
        }
    }

    public List<String> parseData(String source, String type) {
        List<String> res = new ArrayList<>();
        switch (type) {
            case "key方式1": {
                String p = "key=(.*?)&";
                Pattern r = Pattern.compile(p);
                Matcher m = r.matcher(source);
                while (m.find()) {
                    String key = m.group(0).substring(4, m.group(0).length() - 1);
                    res.add(key);
                }
                break;
            }
            case "key方式2": {
                String p = "key=(.{32})";
                Pattern r = Pattern.compile(p);
                Matcher m = r.matcher(source);
                while (m.find()) {
                    String key = m.group(0).substring(4);
                    res.add(key);
                }
                break;
            }
            case "linkKey方式1": {
                String p = "linkKey=(.*?)&";
                Pattern r = Pattern.compile(p);
                Matcher m = r.matcher(source);
                while (m.find()) {
                    String linkKey = m.group(0).substring(8, m.group(0).length() - 1);
                    res.add(linkKey);
                }
                break;
            }
            case "linkKey方式2": {
                String p = "linkKey=(.{94})";
                Pattern r = Pattern.compile(p);
                Matcher m = r.matcher(source);
                while (m.find()) {
                    String linkKey = m.group(0).substring(8);
                    res.add(linkKey);
                }
                break;
            }
            case "roleId方式1": {
                String p = "roleId=(.*?)&";
                Pattern r = Pattern.compile(p);
                Matcher m = r.matcher(source);
                while (m.find()) {
                    String linkKey = m.group(0).substring(7, m.group(0).length() - 1);
                    res.add(linkKey);
                }
                break;
            }
            case "roleId方式2": {
                String p = "roleId=(.{8})";
                Pattern r = Pattern.compile(p);
                Matcher m = r.matcher(source);
                while (m.find()) {
                    String linkKey = m.group(0).substring(7);
                    res.add(linkKey);
                }
                break;
            }
        }
        return res;
    }

    public void changeCookieInfo(JDCouponsCookieDTO dto) {
        JdCouponsCookieSecond jdCouponsCookie = new JdCouponsCookieSecond();
        jdCouponsCookie.setPhone(dto.getPhone());
        JdCouponsCookieSecond cookieEntity = repository.findByPhone(dto.getPhone());
        if (dto.getCookies() != null) {
            cookieEntity.setCookies(dto.getCookies());
        }
        if (dto.getWxpush() != null) {
            cookieEntity.setWxpush(dto.getWxpush());
        }
        if (dto.getUsedate() != null) {
            cookieEntity.setUsedate(dto.getUsedate());
        }
        if (dto.getNeedsuccesspush() != null) {
            cookieEntity.setNeedsuccesspush(dto.getNeedsuccesspush());
        }
        repository.save(cookieEntity);
    }

    public void insertCookie(JDCouponsCookieDTO dto) {
        JdCouponsCookieSecond jdCouponsCookie = new JdCouponsCookieSecond();
        jdCouponsCookie.setCookies(dto.getCookies());
        jdCouponsCookie.setWxpush(dto.getWxpush());
        jdCouponsCookie.setNeedsuccesspush(dto.getNeedsuccesspush());
        jdCouponsCookie.setUsedate(dto.getUsedate());
        jdCouponsCookie.setUsername(dto.getUsername());
        jdCouponsCookie.setPhone(dto.getPhone());
        repository.save(jdCouponsCookie);
    }

    public void getCouponsFromFanLi(String url){
        String p = "&q=(.*?)&";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(url);
        if (m.find()) {
            String s = m.group(0).substring(3, m.group(0).length() - 1);
            String body = "{\"sku\":\"\",\"q\":\""+s+"\",\"d\":\"\",\"platform\":3,\"eid\":\"-1\",\"fp\":\"-1\",\"shshshfp\":\"-1\",\"shshshfpa\":\"-1\",\"shshshfpb\":\"-1\",\"childActivityUrl\":\""+url+"\",\"pageClickKey\":\"MJDAlliance_CheckDetail\"}";
            String sendUrl = "https://api.m.jd.com/api?functionId={functionId}&client={client}&appid={appid}&clientVersion={clientVersion}&loginType={loginType}&body={body}";
            Map<String, Object> data = new HashMap<>();
            data.put("functionId","getUnionGiftCoupon");
            data.put("client","wh5");
            data.put("appid","u");
            data.put("clientVersion","1.0.0");
            data.put("loginType","2");
            data.put("body",body);
            String detail = getCouponsDetailFromFanLi(s,body);
            Iterable<JdCouponsCookieSecond> all = repository.findAll();
            for (JdCouponsCookieSecond cookieEntity : all) {
                try {
                    String header = "{" +
                            "  \"cookie\": \""+cookieEntity.getCookies()+"\",\n" +
                            "  \"origin\": \"https://jingfen.jd.com\",\n" +
                            "  \"referer\": \"https://jingfen.jd.com/item?u_act_p=coupon&q=EUEaEEc8F3UUExVmFSUXRhNsGXETRxZvFnARF0BtQHZBQSRmFnMQFRxmGUFVQUs6RDJ8YnV9F3RFQEFnQHNBHBBqFXYREh1mFnIbExFsEngRQh1vFHIiHRZoGHIWEhVvIA==&cu=true&utm_source=kong&utm_medium=jingfen&utm_campaign=t_1001532273_&utm_term=51b9e2262c7047729c2767c7a8a4feaf\",\n" +
                            "  \"user-agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\"\n" +
                            "}";
                    String resTemp = httpUtil.getWithParam(sendUrl, header,data,false);
                    JSONObject object = JSONObject.parseObject(resTemp);
                    try {
                        String msg = object.getString("msg");
                        if(msg.equals("用户未登录")){
                            log.info("账号{}未登录！", cookieEntity.getPhone());
                            wxMessageUtil.sendZhiXiMessage(cookieEntity.getWxpush(), "账号" + cookieEntity.getPhone() + "ck失效！", detail);
                            continue;
                        }
                    }catch (Exception e){
                        log.info("账号{},出错,返回值为{}",cookieEntity.getPhone(),object.toJSONString(),e);
                    }
                    try {
                        String msg = object.getJSONObject("data").getJSONObject("commonCoupon").getString("msg");
                        if(msg.equals("成功")){
                            log.info("账号{}成功领取优惠券！", cookieEntity.getPhone());
                            if (cookieEntity.getNeedsuccesspush().equals("是")) {
                                wxMessageUtil.sendZhiXiMessage(cookieEntity.getWxpush(), "账号" + cookieEntity.getPhone() + "成功领取优惠券！", detail);
                            }
                        }else if(msg.equals("您已经参加过此活动，别太贪心哟，下次再来~")||msg.equals("您今天已经参加过此活动，别太贪心哟，明天再来~")){
                            log.info("账号{}已经领取过优惠券！",cookieEntity.getPhone());
                        }else if(msg.equals("此券已经被抢完了，下次记得早点来哟~")){
                            log.info("此券已经被抢完了，下次记得早点来哟~");
                            return;
                        }else if (msg.equals("活动已结束或不存在，看下其他活动吧~")){
                            log.info("活动已结束或不存在，看下其他活动吧~");
                            return;
                        }else if(msg.equals("此券今日已经被抢完，请您明日再来~")){
                            log.info("此券今日已经被抢完，请您明日再来~");
                            return;
                        }
                        else{
                            log.info("出错{}---{}",detail,msg);
                            wxMessageUtil.sendZhiXiMessage(cookieEntity.getWxpush(), "账号" + cookieEntity.getPhone() + "领券出错！", detail + object.toJSONString());
                        }
                    }catch (Exception e){
                        log.info("账号{},出错,返回值为{}",cookieEntity.getPhone(),object.toJSONString(),e);
                    }
                }catch (Exception e){
                    log.info("账号{},出错。",cookieEntity.getPhone(),e);
                }

            }
        }
    }
    public String getCouponsDetailFromFanLi(String q,String body){
        Map<String, Object> data = new HashMap<>();
        data.put("functionId","showUnionGiftCoupon");
        data.put("client","wh5");
        data.put("appid","u");
        data.put("clientVersion","1.0.0");
        data.put("loginType","2");
        data.put("body",body);
        String sendUrl = "https://api.m.jd.com/api?functionId={functionId}&client={client}&appid={appid}&clientVersion={clientVersion}&loginType={loginType}&body={body}";
        String header = "{" +
                "  \"origin\": \"https://jingfen.jd.com\",\n" +
                "  \"referer\": \"https://jingfen.jd.com/item?u_act_p=coupon&q=EUEaEEc8F3UUExVmFSUXRhNsGXETRxZvFnARF0BtQHZBQSRmFnMQFRxmGUFVQUs6RDJ8YnV9F3RFQEFnQHNBHBBqFXYREh1mFnIbExFsEngRQh1vFHIiHRZoGHIWEhVvIA==&cu=true&utm_source=kong&utm_medium=jingfen&utm_campaign=t_1001532273_&utm_term=51b9e2262c7047729c2767c7a8a4feaf\",\n" +
                "  \"user-agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\"\n" +
                "}";
        String resTemp = httpUtil.getWithParam(sendUrl, header,data,false);
        JSONObject object = JSONObject.parseObject(resTemp);
        String limitStr = object.getJSONObject("data").getJSONObject("commonCoupon").getString("limitStr");
        String quota = String.valueOf(object.getJSONObject("data").getJSONObject("commonCoupon").getInteger("quota"));
        String discount = String.valueOf(object.getJSONObject("data").getJSONObject("commonCoupon").getInteger("discount"));
        String res = limitStr + "[" + quota + "-" + discount + "]";
        return res;
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void dayDecrease() {
        Iterable<JdCouponsCookieSecond> all = repository.findAll();
        for (JdCouponsCookieSecond cookieEntity : all) {
            cookieEntity.setUsedate(cookieEntity.getUsedate() - 1);
            wxMessageUtil.sendZhiXiMessage(cookieEntity.getWxpush(), "账号" + cookieEntity.getPhone() + "使用时间还剩：" + cookieEntity.getUsedate() + "天", "账号" + cookieEntity.getPhone() + "使用时间还剩：" + cookieEntity.getUsedate());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repository.save(cookieEntity);
        }
    }
}
