package com.xishi.user.entity.vo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 认证的请求dto
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:00
 */
@Data
@Api(value = "AuthRequestVo",description = "实名申请")
public class AuthRequestVo {
    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("不用传")
    private Long userId;

    @ApiModelProperty("不用传")
    private String phone;
}
