package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "BankAccountTypeVo", description = "账户分类列表")
public class BankAccountTypeVo {

    @ApiModelProperty("账户类型id")
    private Integer id;

    @ApiModelProperty("账户类型")
    private String name;

    @ApiModelProperty("账户号码 是否开启：0：未开启 1：开启")
    private Integer acount;

    @ApiModelProperty("账户二维码 是否开启：0：未开启 1：开启")
    private Integer code;

}
