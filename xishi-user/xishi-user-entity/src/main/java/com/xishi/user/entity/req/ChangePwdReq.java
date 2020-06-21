package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "changePwdReq", description = "修改或重置密码")
public class ChangePwdReq {

    @ApiModelProperty("重置密码时的手机号")
    private String phone;

    @ApiModelProperty("重置密码时的验证码")
    private String code;

    @ApiModelProperty("修改密码时的老密码")
    private String oldPwd;

    @ApiModelProperty("密码")
    private String pwd;

    @ApiModelProperty("确认的密码")
    private String confirmPwd;

    @ApiModelProperty("后端用的，不用填")
    private Long userId;
}
