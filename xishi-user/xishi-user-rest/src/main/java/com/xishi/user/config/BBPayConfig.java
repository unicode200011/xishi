package com.xishi.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pay")
@Profile(value = {"local", "test", "prod"})
@Data
public class BBPayConfig {
    private String env;
    private String baseUrl;
    private String MerCode;
    private String CoinCode;
    private String KeyB;
    private String desKey;

    public String getAddUserUrl(){
        return this.baseUrl +"/AddUser";
    }

    public String getAddressUrl(){
        return this.baseUrl +"/GetAddress";
    }

    public String getLoginUrl(){
        return this.baseUrl +"/Login";
    }

    public String getOrderDetailUrl(){
        return this.baseUrl +"/OrderDetail";
    }

}
