package com.xishi.user.util;

import com.alibaba.fastjson.JSONObject;
import com.common.base.util.HttpKit;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 长网址转换短网址
 *
 * @author: lx
 */
public class LongUrlToShortUrl {

    public static final String APPKEY = "5bce7034160892069f8c423349d7e12b";
    public static final String URL = "http://www.mynb8.com/api/sina";


    /**
     * 转换
     *
     * @param url
     * @return
     */
    public static String conversion(String url) {
        try {
            String newUrl = URLEncoder.encode(url, "UTF-8");
            String sign = DigestUtils.md5Hex(APPKEY + DigestUtils.md5Hex(newUrl).toLowerCase()).toLowerCase();
            Map<String, String> params = new HashMap<>(16);
            params.put("appkey", APPKEY);
            params.put("long_url", url);
            params.put("sign", sign);
            String resultStr = HttpKit.sendGet(URL, params);
            Map map = JSONObject.parseObject(resultStr, Map.class);
            String rsCode = map.get("rs_code").toString();
            if ("0".equals(rsCode)) {
                Map data = JSONObject.parseObject(map.get("data").toString(), Map.class);
                return data.get("short_url").toString();
            } else {
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.err.println(conversion("http://www.gamesource.cn"));
    }
}
