package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "BBPayWayVo", description = "请求充值")
public class BBPayWayVo{

    @ApiModelProperty("支付方式id")
    private Integer id;

    @ApiModelProperty("支付方式别名")
    private String name;
}
