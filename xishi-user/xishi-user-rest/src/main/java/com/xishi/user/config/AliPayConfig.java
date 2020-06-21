package com.xishi.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AliPayConfig {

    @Value(value = "${alipay.appId}")
    public String appId;

    @Value(value = "${alipay.pid}")
    public String pid;

    @Value(value = "${alipay.privateKey}")
    public String privateKey;

    @Value(value = "${alipay.publicKey}")
    public String publicKey;

    @Value(value = "${alipay.notifyUrl}")
    public String notifyUrl;

    @Value(value = "${alipay.signType}")
    public String signType;

    @Value(value = "${alipay.charset}")
    public String charset;

    @Value(value = "${alipay.requestUrl}")
    public String requestUrl;

    @Value(value = "${alipay.queryUrl}")
    public String queryUrl;

    @Value(value = "${alipay.identifyCallbackUrl}")
    public String identifyCallbackUrl;
}
