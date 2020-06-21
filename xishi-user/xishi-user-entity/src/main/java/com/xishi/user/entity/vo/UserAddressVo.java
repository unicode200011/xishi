package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "userAddressVo", description = "用户收货地址")
public class UserAddressVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("省市区 格式 省-市-区")
    private String address;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("0 不默认 1 默认")
    private Integer type;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("操作类型 删除才传值,1--删除")
    private Integer opType;

}
