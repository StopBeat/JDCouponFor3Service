package com.example.jdcouponforotherservice.WX.application.service;


import com.example.jdcouponforotherservice.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnalysisURL {
    @Autowired
    HttpUtil httpUtil;

    public List<String> analysisURL(String msg) {
        List<String> res = new ArrayList<>();
        if (msg.contains("https://u.jd.com/")) {
            String p = "https://u.jd.com/(.{7})";
            Pattern r = Pattern.compile(p);
            Matcher m = r.matcher(msg);
            String headers = "{\"user-agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36\"}";
            while (m.find()) {
                String url = m.group(0);
                String html = httpUtil.getRequestFromHTML(url, headers, false);
                String redUrl = getUrl(html);
                if(redUrl.contains("u_act_p=coupon")){
                    res.add(redUrl);
                }
            }
        }
        return res;
    }

    public String getUrl(String html) {
        String p = "var hrl=(.*?);";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(html);
        if (m.find()) {
            String url = m.group(0).substring(9, m.group(0).length() - 2);
            String header = "{\n" +
                    "    \"Host\": \"u.jd.com\",\n" +
                    "    \"Connection\": \"keep-alive\",\n" +
                    "    \"User-Agent\":\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1941.0 Safari/537.36\",\n" +
                    "    \"Accept\": \"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\",\n" +
                    "    \"Referer\": \"https://u.jd.com/9MrQNgD\",\n" +
                    "    \"Accept-Encoding\": \"gzip, deflate, br\",\n" +
                    "    \"Accept-Language\": \"zh-CN,zh;q=0.9,en;q=0.8,ru;q=0.7\"," +
                    " \"Cookie\": \"unionuuid=V2_ZQIUW0BeQxMmCRUDfh9eADRQE1gRBEsXclpEV3JMCQIIABNeQVZLFnwIQld%2fGFRqYAINXURRXB1qCEJSbgxJ; __jda=122270672.16643498987381271848299.1664349898.1664349898.1664349898.1; __jdc=122270672; mba_muid=16643498987381271848299; _gia_s_local_fingerprint=fa3f075dcb0dfe43ffa1d4a0fa8638aa; _gia_s_e_joint={\\\"eid\\\":\\\"ARMMYDFAJ2WO7F4PZDQQAHRPKRFOCNJKZLBMU7CHSGXBDFVMAUCXPR6UCG5PM7HZOKJK5AYN6ZB6G36BYOCU463QAI\\\",\\\"ma\\\":\\\"\\\",\\\"im\\\":\\\"\\\",\\\"os\\\":\\\"Windows 10\\\",\\\"ip\\\":\\\"60.177.9.157\\\",\\\"ia\\\":\\\"\\\",\\\"uu\\\":\\\"\\\",\\\"at\\\":\\\"5\\\"}; 3AB9D23F7A4B3C9B=ARMMYDFAJ2WO7F4PZDQQAHRPKRFOCNJKZLBMU7CHSGXBDFVMAUCXPR6UCG5PM7HZOKJK5AYN6ZB6G36BYOCU463QAI; CSID=Ez06E3BeXVFRQwUIWUMIE1FlcXpyeQQIRVdUVxcOXwRqe3Z7dX91ch9RVy1TWlRXWndvYA5eRBRrZBlwXFpYOkBTQFxBWVlRe3d9cw%3d%3d; unpl=JF8EALNnNSttWEMBAx0BHRcUSVoEWwhYHEdUZ28MVlhfG1IFHwsbGkd7XlVdXhRKHx9sbhRUWVNJVQ4aAisSFHtdVV9dDEkUAGllNWRVUCVUSBtsGHwTBhAZbl4IexYzb2ACUV9dQ10NEgQZGxBIW1RXWAFIHwtfVwVSbVhKVQQbCxsTFU5YVm5tCXsWM25XUzpdWUpVBBIHHhEVBl1TWVgKTh8KZ24DVlRYSFIFEgcSERhDbVVuXg; CCC_SE=ADC_GM2qFw6dgTZPQKMXhY3lsY38G2y1osVImsqYkFcov1zsemlpJYswSJ4g1EvMSXXNW40FB%2b%2bLR%2f5mXEfHmrF%2f4DjGovqhCW6QVu%2f3dWZN4u8RPbwhaYCewhftoYWS9grxWJUGnkamAaK7%2fACc%2bwF1XCAchIjPLerOH9JIB8m68JqbY33NEwMtBOhGOTrcYZ%2bFlUVCgynYa9NhmG0sEqMD%2bqiI0hcDoXQIbOsP02M8CHCjr3ZSSmRQYdGL3%2fFQv5U9tWdypOFy80X0t%2fi0kwIwWR2okFbS9lzbjaqRjVwGA37GItCdY%2fV6G2trYYQNvcMyFKtDOeTGmrnX6bqm3EbbtRvbV6Y%2fIiAL8cdEu1BvULLEDbzIFJ2JDDJNISnVwQTm1j5JUEjMg4PfZyHo3Dw%2fjdxZEVU4tIYt6WM8xKXxaJAhgEJkjoPjOAYFgPTlfb5n%2fJ%2biGBH1GEV9U7qcU6JDE5lYacTIBbBTXeM%2bC9N2Ky1s27OahirVd7katSXMcB5PDjfSnuTehltXB3Vyng9Afw%3d%3d; __jdv=122270672%7Ckong%7Ct_1001532273_%7Cjingfen%7C19d77274536a4dafaf998346a715819f%7C1664350202000; __jd_ref_cls=MDownLoadFloat_TopOldExpo; RT=\\\"z=1&dm=jd.com&si=5v0mtvdkpzu&ss=l8lb4qka&sl=2&tt=ib4&ld=ad9\\\"\"" +
                    "  }";
            ResponseEntity<String> requestResponse = httpUtil.get302Request(url, header, false);
            return String.valueOf(requestResponse.getHeaders().getLocation());

        }
        return "";
    }
}
