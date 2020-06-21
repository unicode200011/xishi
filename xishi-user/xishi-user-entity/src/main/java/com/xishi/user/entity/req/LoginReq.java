package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "loginReq", description = "登录请求")
public class LoginReq {

    //账户密码登录
    public static final int ACCOUNT_LOGIN = 0;

    //第三方登录
    public static final int OTHER_LOGIN = 1;

    //验证码登录
    public static final int CODE_LOGIN = 2;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String pwd;

    @ApiModelProperty("登录方式 0--手机号，1--第三方，2--验证码")
    private Integer loginType = 0;

    @ApiModelProperty("三方登录时 第三方登录下哪个第三方 0--支付宝,1--微信")
    private Integer thirdType = 0;

    @ApiModelProperty("手机设备的唯一标识,调用方不用传,直接从hearder获取")
    private String deviceToken;
}
