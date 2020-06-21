package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@ApiModel(value = "BBLoginResponse", description = "币宝登录货币交易返回信息")
public class BBLoginResponse {

    private boolean Success;

    private int Code;

    private String Message;

    private LoginData Data;

    class LoginData{
        private String Url;
        private String Token;

        public String getUrl(){
            return this.Url;
        }
        public void setUrl(String url){
           this.Url = url;
        }
        public String getToken(){
            return this.Token;
        }
        public void setToken(String token){
            this.Token = token;
        }
    }
}
