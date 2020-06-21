package com.xishi.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayUserCertifyOpenCertifyRequest;
import com.alipay.api.request.AlipayUserCertifyOpenInitializeRequest;
import com.alipay.api.request.AlipayUserCertifyOpenQueryRequest;
import com.alipay.api.response.AlipayUserCertifyOpenCertifyResponse;
import com.alipay.api.response.AlipayUserCertifyOpenInitializeResponse;
import com.alipay.api.response.AlipayUserCertifyOpenQueryResponse;
import com.xishi.user.config.AliPayConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Component
public class AliIdentifyAuthUtil {

    private static Logger logger = LoggerFactory.getLogger(AliIdentifyAuthUtil.class);

    private static final String EXPIRE_TIME = "15m";
    private static final String FORMAT = "json";

    private static String APP_ID;
    public static String NOTIFY_URL;
    private static String CHARSET;
    private static String SIGN_TYPE;
    private static String PRIVATE_KEY;
    private static String PUBLIC_KEY;
    private static String URL;
    private static String IDENTIFY_CALLBACK_URL;
    private static AlipayClient alipayClient;

    @Autowired
    private AliPayConfig aliPay;

    @PostConstruct
    public void init() {
        APP_ID = aliPay.appId;
        NOTIFY_URL = aliPay.notifyUrl;
        CHARSET = aliPay.charset;
        SIGN_TYPE = aliPay.signType;
        PRIVATE_KEY = aliPay.privateKey;
        PUBLIC_KEY =aliPay.getPublicKey();
        URL = aliPay.getRequestUrl();
        IDENTIFY_CALLBACK_URL = aliPay.getIdentifyCallbackUrl();

        alipayClient = new DefaultAlipayClient(URL, APP_ID, PRIVATE_KEY, FORMAT, CHARSET, PUBLIC_KEY, SIGN_TYPE);
    }

    //初始化身份认证
    public static String initIdentify(String name,String idcard,boolean callbackOpenZfb) {
        AlipayUserCertifyOpenInitializeRequest request = new AlipayUserCertifyOpenInitializeRequest();

        //构造身份信息json对象
        JSONObject identityObj = new JSONObject();
        //身份类型
        identityObj.put("identity_type", "CERT_INFO");
        //证件类型
        identityObj.put("cert_type", "IDENTITY_CARD");
        //真实姓名
        identityObj.put("cert_name", name);
        //证件号码
        identityObj.put("cert_no", idcard);
        logger.info("AliIdentifyAuthUtil initIdentify identity_param={}",identityObj);

        //构造商户配置json对象
        JSONObject merchantConfigObj = new JSONObject();
        // 设置回调地址, 如果需要直接在支付宝APP里面打开回调地址使用alipay协议，appId用固定值 20000067，url替换为urlEncode后的业务回跳地址
        // alipays://platformapi/startapp?appId=20000067&url=https%3A%2F%2xxx.com%dd%bb
        String callback = IDENTIFY_CALLBACK_URL;
        if(callbackOpenZfb) {
            try{
                callback =  URLEncoder.encode(callback,"UTF-8");
            }catch (Exception e) {
            }
            callback= "alipays://platformapi/startapp?appId=20000067&url="+ callback;
        }
        merchantConfigObj.put("return_url", callback);
        logger.info("AliIdentifyAuthUtil initIdentify merchant_config={}",merchantConfigObj);

        //构造身份认证初始化服务业务参数数据
        JSONObject bizContentObj = new JSONObject();
        //商户请求的唯一标识，推荐为uuid
        bizContentObj.put("outer_order_no", UUID.randomUUID().toString());
        bizContentObj.put("biz_code", "FACE");
        bizContentObj.put("identity_param", identityObj);
        bizContentObj.put("merchant_config", merchantConfigObj);
        logger.info("AliIdentifyAuthUtil initIdentify biz_content={}",bizContentObj);

        request.setBizContent(bizContentObj.toString());
        try {
            //发起请求
            AlipayUserCertifyOpenInitializeResponse response = alipayClient.execute(request);
            logger.info("AliIdentifyAuthUtil initIdentify response={} ", response);
            if (response.isSuccess()) {
                String certifyId = response.getCertifyId();
                logger.info("AliIdentifyAuthUtil initIdentify result certifyId={}", certifyId);
                return certifyId;
            }
            return null;
        }catch(Exception e ) {
            logger.error("AliIdentifyAuthUtil initIdentify error,",e);
            return null;
        }
    }

    //初始化后,生成认证请求
    public static String genIdentifyUrl(String certifyId,boolean wakeupApp) {
        AlipayUserCertifyOpenCertifyRequest request = new AlipayUserCertifyOpenCertifyRequest();

        //设置certifyId
        JSONObject bizContentObj = new JSONObject();
        bizContentObj.put("certify_id", certifyId);
        request.setBizContent(bizContentObj.toString());

        try {
            //生成请求链接，这里一定要使用GET模式
            AlipayUserCertifyOpenCertifyResponse response = alipayClient.pageExecute(request, "GET");
            logger.info("AliIdentifyAuthUtil genIdentifyUrl certifyId={}，response={} ",certifyId, response);
            if (response.isSuccess()) {
                String certifyUrl = response.getBody();
                logger.info("AliIdentifyAuthUtil genIdentifyUrl result certifyId={},certifyUrl={}",certifyId, certifyUrl);
                if(wakeupApp) {
                    //需要在App中唤起支付宝客户端
                    String returnUrl=certifyUrl;
                    try{
                        returnUrl =  URLEncoder.encode(returnUrl,"UTF-8");
                    }catch (Exception e) {
                    }
                    returnUrl= "alipays://platformapi/startapp?appId=20000067&url="+ returnUrl;
                    return returnUrl;
                }
                return certifyUrl;
            }
            return null;
        }catch(Exception e ) {
            logger.error("AliIdentifyAuthUtil genIdentifyUrl error,certifyId={}",certifyId,e);
            return null;
        }
    }

    //查询认证结果
    public static boolean queryIdentifyResult(String certifyId) {
        AlipayUserCertifyOpenQueryRequest request = new AlipayUserCertifyOpenQueryRequest();
        //设置certifyId
        JSONObject bizContentObj = new JSONObject();
        //certifyId是初始化接口返回
        bizContentObj.put("certify_id", certifyId);
        request.setBizContent(bizContentObj.toString());

        try {
            AlipayUserCertifyOpenQueryResponse response = alipayClient.execute(request);
            logger.info("AliIdentifyAuthUtil queryIdentifyResult certifyId={}，response={} ",certifyId, JSON.toJSONString(response));
            if (response.isSuccess()) {
                //passed 	通过为T，不通过为F
                //"body": "{\"alipay_user_certify_open_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"passed\":\"F\"},\

                String passed="";
                //实际结果解析passed的解法
                String body = response.getBody();
                logger.info("AliIdentifyAuthUtil queryIdentifyResult certifyId={}，body={} ",certifyId, body);
                if(StringUtils.isNotBlank(body)) {
                    JSONObject jsonObject = JSON.parseObject(body);
                    JSONObject certifyObject =jsonObject.getJSONObject("alipay_user_certify_open_query_response");
                    if(certifyObject!=null) {
                        Object passObj = certifyObject.get("passed");
                        passed = passObj==null?"":passObj.toString();
                    }
                }
                logger.info("AliIdentifyAuthUtil queryIdentifyResult certifyId={}，passed={} ",certifyId, passed);
                if(StringUtils.isNotBlank(passed)) {
                    if("T".equalsIgnoreCase(passed)) {
                        return true;
                    }
                    return false;
                }

                //api解析结果的解法
                List<String> passedList =response.getPassed();
                String passedStr = passedList==null?"": JSON.toJSONString(passedList);
                logger.info("AliIdentifyAuthUtil queryIdentifyResult result certifyId={},passedList={}",certifyId,passedStr);
                if(passedList!=null && passedList.contains("T")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("AliIdentifyAuthUtil genIdentifyUrl error,certifyId={}",certifyId,e);
            return false;
        }
    }
}
