package com.xishi.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * system parameter config
 */
@Component
@PropertySource(value = "classpath:config/conf.properties", encoding = "UTF-8")
@Data
public class SystemConfig {
    /**
     * sms prefix
     */
    @Value(value = "${sm.sms.prefix}")
    private String smsPrefix;
    /**
     * sms expire time
     */
    @Value(value = "${sm.sms.expireTime}")
    private Long smsExpireTime;
    /**
     * sms app key
     */
    @Value(value = "${sm.sms.appKey}")
    private String smsAppKey;
    /**
     * sms app secret
     */
    @Value(value = "${sm.sms.appSecret}")
    private String smsAppSecret;
    /**
     * sms temp code
     */
    @Value(value = "${sm.sms.tempCode}")
    private String smsTempCode;
    /**
     * sms sign name
     */
    @Value(value = "${sm.sms.signName}")
    private String smsSignName;
    /**
     * sms region
     */
    @Value(value = "${sm.sms.region}")
    private String smsRegion;

}
