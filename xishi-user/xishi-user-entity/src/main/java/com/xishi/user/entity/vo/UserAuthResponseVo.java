package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "UserAuthResponseVo", description = "用户实名认证返回信息")
public class UserAuthResponseVo {


    @ApiModelProperty("是否已支付 0--未支付，1--已支付")
    private Integer hasPay;

    @ApiModelProperty("用户实名认证次数")
    private Integer authTime;

    @ApiModelProperty("实名未通过原因")
    private String reason;


}
