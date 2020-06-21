package com.xishi.user.util.pay;

import com.alibaba.fastjson.JSON;
import com.xishi.user.config.WxPayConfig;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 微信支付
 *
 * @author: lx
 */

@Component
public class WxPay {

    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    public static String APP_ID;
    public static String MCH_ID;
    public static String API_KEY;
    public static String CHARSET;
    public static String SIGN_TYPE;
    public static String NOTIFY_URL;
    public static String UNIFIED_ORDER_URL;

    @Autowired
    private WxPayConfig wxPayConfig;

    @PostConstruct
    public void init() {
        APP_ID = wxPayConfig.appId;
        MCH_ID = wxPayConfig.mchId;
        API_KEY = wxPayConfig.apiKey;
        CHARSET = wxPayConfig.charset;
        SIGN_TYPE = wxPayConfig.signType;
        NOTIFY_URL = wxPayConfig.notifyUrl;
        UNIFIED_ORDER_URL = wxPayConfig.requestUrl;
    }

    /**
     * 支付参数
     */
    public static class WxPayParam {
        private BigDecimal payMoney;
        private String body;
        private String orderNo;
        private Map<String, String> attach;
        private HttpServletRequest request;
        private PayType payType;

        public WxPayParam(BigDecimal payMoney, String body, String orderNo, Map<String, String> attach, HttpServletRequest request, PayType payType) {
            this.payMoney = payMoney;
            this.body = body;
            this.orderNo = orderNo;
            this.attach = attach;
            this.request = request;
            this.payType = payType;
        }

        public BigDecimal getPayMoney() {
            return payMoney;
        }

        public String getBody() {
            return body;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public Map<String, String> getAttach() {
            return attach;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public PayType getPayType() {
            return payType;
        }
    }

    /**
     * https信任管理器
     */
    public static class MyX509TrustManager implements X509TrustManager {

        // 检查客户端证书
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        // 检查服务器端证书
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        // 返回受信任的X509证书数组
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * 支付类型
     */
    public enum PayType {
        APP("APP"), //APP支付
        JSAPI("JSAPI"), //公众号支付
        NATIVE("NATIVE"); //扫码支付

        private String sign;

        PayType(String sign) {
            this.sign = sign;
        }
    }

    /**
     * @description: 发起支付
     * @author: lx
     */
    public static SortedMap<Object, Object> wxPay(WxPayParam wxPayParam) throws Exception {
        SortedMap<Object, Object> resultParameter = new TreeMap<>();
        SortedMap<Object, Object> params = new TreeMap<>();

        Map<String, String> attachParams = wxPayParam.getAttach();
        String body = wxPayParam.getBody();
        String orderNum = wxPayParam.getOrderNo();
        BigDecimal rmb = wxPayParam.getPayMoney();
        HttpServletRequest request = wxPayParam.getRequest();
        PayType payType = wxPayParam.getPayType();

        switch (payType) {
            case APP:
                params.put("total_fee", String.valueOf(rmb.multiply(BigDecimal.valueOf(100)).intValue()));
                params.put("attach", JSON.toJSONString(attachParams));
                params.put("appid", APP_ID);
                params.put("mch_id", MCH_ID);
                params.put("nonce_str", generateNonceStr());
                params.put("body", body);
                params.put("out_trade_no", orderNum);
                params.put("spbill_create_ip", getIpAddress(request));
                params.put("notify_url", NOTIFY_URL);
                params.put("trade_type", payType.sign);
                params.put("sign", createSign(CHARSET, params));
                //请求参数转换
                String requestXML = getRequestXml(params);
                //下单
                String result = httpsRequest(UNIFIED_ORDER_URL, "POST", requestXML);
                //APP请求参数
                Map<String, String> map = doXMLParse(result);
                if (map.get("return_code").equals(FAIL)) {
                    resultParameter.put("sign", FAIL);
                    resultParameter.put("msg", map.get("return_msg"));
                    return resultParameter;
                }
                resultParameter.put("appid", APP_ID);
                resultParameter.put("partnerid", MCH_ID);
                resultParameter.put("prepayid", map.get("prepay_id"));
                resultParameter.put("package", "Sign=WXPay");
                resultParameter.put("noncestr", generateNonceStr());
                resultParameter.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis() / 1000)));
                String paySign = createSign(CHARSET, resultParameter);
                resultParameter.put("sign", paySign);
//                resultParameter.put("_package", "Sign=WXPay"); //这里是防止package字段与APP使用语言关键字有冲突,如果有就放开这段注释
                break;
            case JSAPI:

                break;
            case NATIVE:

                break;

            default:

                break;
        }

        return resultParameter;
    }


    /**
     * @description: 签名
     * @author: lx
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 回调数据解析
     *
     * @param request
     * @return
     */
    public static SortedMap<Object, Object> parseWxNotify(HttpServletRequest request) {
        try {
            InputStream inputStream = request.getInputStream();
            StringBuilder sb = new StringBuilder();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            //格式转换
            Map<String, String> notifyMap = WxPay.xmlToMap(sb.toString());
            SortedMap<Object, Object> packageParams = new TreeMap();
            Iterator it = notifyMap.keySet().iterator();
            while (it.hasNext()) {
                String parameter = (String) it.next();
                String parameterValue = notifyMap.get(parameter);
                String v = "";
                if (null != parameterValue) {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            return packageParams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description: 发送Https请求
     * @author: lx
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 验签
     * @author: lx
     */
    public static boolean isWxPaySign(String characterEncoding, SortedMap<Object, Object> packageParams) {
        StringBuilder sb = new StringBuilder();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

        // 计算摘要
        String mySign = MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String wxSign = ((String) packageParams.get("sign")).toLowerCase();

        return wxSign.equals(mySign);
    }

    /**
     * @description: 将请求参数转换为xml格式的string
     * @author: lx
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * @description: 解析xml, 返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @author: lx
     */
    public static Map doXMLParse(String strXml) throws JDOMException, IOException {
        strXml = strXml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strXml || "".equals(strXml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strXml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v;
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }
        in.close();
        return m;
    }

    /**
     * @description: XML格式字符串转换为Map
     * @author: lx
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                throw ex;
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }

    /**
     * @description: 将Map转换为XML格式的字符串
     * @author: lx
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * @description: 获取子结点的xml
     * @author: lx
     */
    public static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    //======================= MD5 ======================//
    private static String byteArrayToHexString(byte b[]) {
        StringBuilder resultSb = new StringBuilder();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetName) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
        } catch (Exception exception) {
        }
        return resultString;
    }
    //======================= MD5 ======================//

    /**
     * @description: 获取随机字符串
     * @author: lx
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * @description: 获取IP;
     * @author: lx
     */
    public final static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-real-ip");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}
