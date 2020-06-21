package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "UserWalletVo", description = "钱包详情")
public class UserWalletVo {

    @ApiModelProperty("西施币")
    private BigDecimal gbMoeny;

    @ApiModelProperty("消费总值")
    private BigDecimal giftAmount;

    @ApiModelProperty("合计收到打赏（西施币）")
    private BigDecimal giveAmount;
}
