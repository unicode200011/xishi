package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "accountReq", description = "用户账号")
public class AccountReq {

    @ApiModelProperty("账号 手机号")
    private String account;

    @ApiModelProperty("密码")
    private String pwd;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("第三方账号")
    private String thirdAccount;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("经度")
    private String lgt;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("生日 YYYY-MM-DD")
    private String birthday;

    @ApiModelProperty("注册类型 0--注册,1--后台添加")
    private int regType = 0;

    @ApiModelProperty("后台添加的加密值")
    private String encryParam;

    @ApiModelProperty(" 第三方账号下哪个第三方 0--qq,1--微信")
    private Integer thirdType=0;

}
