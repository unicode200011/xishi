package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "MoneyDetailVo", description = "各种余额")
public class MoneyDetailVo {

    @ApiModelProperty("当前余额")
    private BigDecimal walletAmount;

    @ApiModelProperty("合计收到打赏（西施币）")
    private BigDecimal giftAmount;

    @ApiModelProperty("可提现金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty("换算比例")
    private int rate = 0;

}
