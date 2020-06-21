package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "userAuthVo", description = "用户实名认证信息")
public class UserAuthVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("是否已支付 0--未支付，1--已支付")
    private Integer hasPay;

    @ApiModelProperty("认证状态 0 待认证 1 认证中 2 已认证 3 认证失败")
    private Integer state;

    @ApiModelProperty("实名未通过原因")
    private String reason;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
