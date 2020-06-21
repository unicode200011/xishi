package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "smsReq", description = "短信验证码")
public class SmsReq {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("发送内容")
    private String content;

    @ApiModelProperty("短信验证码标记  0--注册 1--用户修改密码等相关操作")
    private Integer type = 0;

    @ApiModelProperty("发送来源 0--APP，1--H5/浏览器,调用方不填")
    private Integer sourceType;
}
