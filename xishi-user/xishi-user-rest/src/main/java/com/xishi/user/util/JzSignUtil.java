package com.xishi.user.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JzSignUtil {

    public static String createSign(Map<String, String> map, String secretKey) {
        log.info("==========createSign:" + JSON.toJSONString(map) + ";secretKey:" + secretKey);
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            sortMap(infoIds);
            log.info("==========createSign:infoIds:" + infoIds);
            String params = getParamStr(infoIds).toString().replaceFirst("&", "");
            log.info("==========createSign:params:" + params);
            String sign = "";
            sign = HMACSHA256.sha256_HMAC(params, secretKey);
            log.info("====sign{}", sign.toUpperCase());
            return sign.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sortMap(List<Map.Entry<String, String>> infoIds) {
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });
    }

    public static String getParamStr(List<Map.Entry<String, String>> infoIds) {
        //构造签名键值对的格式
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            if (item.getKey() != null || item.getKey() != "") {
                String key = item.getKey();
                String val = item.getValue();
                if (!(val == "" || val == null)) {
                    sb.append("&" + key + "=" + val);
                }
            }
        }
        return sb.toString();
    }


}
