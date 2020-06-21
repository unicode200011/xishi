package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "ChargeVo", description = "充值页信息")
public class ChargeVo{

    @ApiModelProperty("当前余额")
    private BigDecimal amount;

    @ApiModelProperty("换算比例")
    private int rate = 0;

    @ApiModelProperty("充值列表")
    private List<ChargeListVo> chargeListVos;
}
