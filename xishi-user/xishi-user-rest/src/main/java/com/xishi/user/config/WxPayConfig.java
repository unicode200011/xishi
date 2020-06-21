package com.xishi.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 微信配置
 *
 * @author lx
 */
@Component
@Data
public class WxPayConfig {

    @Value(value = "${wx.appId}")
    public String appId;

    @Value(value = "${wx.mchId}")
    public String mchId;

    @Value(value = "${wx.apiKey}")
    public String apiKey;

    @Value(value = "${wx.signType}")
    public String signType;

    @Value(value = "${wx.notifyUrl}")
    public String notifyUrl;

    @Value(value = "${wx.requestUrl}")
    public String requestUrl;

    @Value(value = "${wx.charset}")
    public String charset;
}
