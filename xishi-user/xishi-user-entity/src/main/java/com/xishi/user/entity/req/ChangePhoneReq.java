package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ChangePhoneReq", description = "换绑手机")
public class ChangePhoneReq {

    @ApiModelProperty("新手机号")
    private String phone;

    @ApiModelProperty("新手机号验证码")
    private String code;

    @ApiModelProperty("原手机号验证码")
    private String oldCode;

    @ApiModelProperty("后端用的，不用填")
    private Long userId;
}
