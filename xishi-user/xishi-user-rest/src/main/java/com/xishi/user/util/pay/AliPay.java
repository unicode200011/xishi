package com.xishi.user.util.pay;


import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.common.base.util.DateTimeKit;
import com.common.base.util.MakeOrderNum;
import com.xishi.user.config.AliPayConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 支付宝支付
 *
 * @author: lx
 */

@Component
public class AliPay {

    private static final String EXPIRE_TIME = "15m";
    private static final String FORMAT = "json";

    private static String APP_ID;
    private static String PID;
    public static String NOTIFY_URL;
    private static String CHARSET;
    private static String SIGN_TYPE;
    private static String PRIVATE_KEY;

    @Autowired
    private AliPayConfig aliPay;

    @PostConstruct
    public void init() {
        APP_ID = aliPay.appId;
        PID = aliPay.pid;
        NOTIFY_URL = aliPay.notifyUrl;
        CHARSET = aliPay.charset;
        SIGN_TYPE = aliPay.signType;
        PRIVATE_KEY = aliPay.privateKey;
    }

    /**
     * 支付参数
     */
    @Data
    @AllArgsConstructor
    public static class AliPayParam {
        /**
         * 支付金额
         */
        private BigDecimal payMoney;
        /**
         * 支付描述
         */
        private String body;
        /**
         * 订单号
         */
        private String orderNo;
        /**
         * 业务参数;
         */
        private Map<String, String> attach;
        /**
         * 支付类型
         */
        private PayType payType;
    }

    /**
     * 支付类型
     */
    public enum PayType {
        //APP支付
        QUICK_MSECURITY_PAY("QUICK_MSECURITY_PAY"),
        //手机网站支付
        QUICK_WAP_WAY("QUICK_WAP_WAY");

        PayType(String sign) {
            this.sign = sign;
        }

        private String sign;
    }


    /**
     * @description: 发起支付
     * @author: lx
     */
    public static String aliPay(AliPayParam aliPayParam) throws Exception {
        Map<String, String> params = new HashMap<>();
        StringBuilder signStr = new StringBuilder();

        Map<String, String> publicParams = aliPayParam.getAttach();
        String body = aliPayParam.getBody();
        String orderNo = aliPayParam.getOrderNo();
        BigDecimal payMoney = aliPayParam.getPayMoney();
        PayType payType = aliPayParam.getPayType();
        switch (payType) {

            case QUICK_MSECURITY_PAY:

                params.put("app_id", APP_ID);
                Map<String, String> biz_content = new HashMap<>();
                biz_content.put("subject", body);
                biz_content.put("out_trade_no", orderNo);
                biz_content.put("total_amount", payMoney.toString());
                biz_content.put("product_code", payType.sign);

                params.put("passback_params", JSON.toJSONString(publicParams));
                params.put("biz_content", JSON.toJSONString(biz_content));
                params.put("timeout_express", EXPIRE_TIME);
                params.put("charset", CHARSET);
                params.put("format", FORMAT);
                params.put("method", "alipay.trade.app.pay");
                params.put("notify_url", NOTIFY_URL);
                params.put("sign_type", SIGN_TYPE);
                params.put("timestamp", DateTimeKit.now());
                params.put("version", "1.0");

                List<String> paramsList = new ArrayList<>();
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    paramsList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), CHARSET) + "&");
                }
                // 参数解析
                String signContent = AlipaySignature.getSignContent(params);
                // 签名
                String signs = AlipaySignature.rsaSign(signContent, PRIVATE_KEY, CHARSET,SIGN_TYPE);
                int size = paramsList.size();
                String[] arrayToSort = paramsList.toArray(new String[size]);
                Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
                for (int i = 0; i < size; i++) {
                    signStr.append(arrayToSort[i]);
                }
                signStr.append("sign=" + URLEncoder.encode(signs, CHARSET));

                break;

            case QUICK_WAP_WAY:

                break;

            default:

                break;
        }
        return signStr.toString();
    }

    /**
     * @description: 完整版sdk登录
     * @author: lx
     */
    public static String completeLogin() throws Exception {
        Map<String, String> params = new HashMap<>();
        StringBuilder signStr = new StringBuilder();

        params.put("apiname", "com.alipay.account.auth");
        params.put("method", "alipay.open.auth.sdk.code.get");
        params.put("app_name", "mc");
        params.put("biz_type", "openservice");
        params.put("app_id", APP_ID);
        params.put("pid", PID);
        params.put("product_id", "APP_FAST_LOGIN");
        params.put("scope", "kuaijie");
        params.put("target_id", MakeOrderNum.makeOrderNum());
        params.put("auth_type", "LOGIN");
        params.put("sign_type", SIGN_TYPE);

        List<String> paramsList = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            paramsList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), CHARSET) + "&");
        }
        // 参数解析
        String signContent = AlipaySignature.getSignContent(params);
        // 签名
        String signs = AlipaySignature.rsaSign(signContent, PRIVATE_KEY, CHARSET,SIGN_TYPE);
        int size = paramsList.size();
        String[] arrayToSort = paramsList.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < size; i++) {
            signStr.append(arrayToSort[i]);
        }
        signStr.append("sign=" + URLEncoder.encode(signs, CHARSET));
        return signStr.toString();
    }

    /**
     * 解析支付宝回调参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> parseAliPayNotifyParam(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(16);
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

}
