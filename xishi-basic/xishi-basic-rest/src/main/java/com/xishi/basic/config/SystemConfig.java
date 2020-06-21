package com.xishi.basic.config;

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
    @Value(value = "${sm.sms.expireTime:600}")
    private Long smsExpireTime;

    /**
     * common data expire time
     */
    @Value(value = "${commonData.expireTime:600}")
    private Long commonDataExpireTime;
}
