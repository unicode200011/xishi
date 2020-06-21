package com.xishi.user.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sms")
@Profile(value = {"local", "test", "prod"})
public class SMSConfig {
    private String url;
    private String MerCode;
    private String desKey;
    private String MsgType;
    private String Content;
    private String KeyB;
    private String Telephone;
    private String Timestamp;
}
