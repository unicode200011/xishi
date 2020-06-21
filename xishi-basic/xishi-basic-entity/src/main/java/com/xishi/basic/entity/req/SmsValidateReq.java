package com.xishi.basic.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "smsValidateReq", description = "验证短信验证码")
public class SmsValidateReq {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("验证短信验证码类型 0--公共验证,1--找回密码, 2--验证码登录")
    private Integer type = 0;

}
