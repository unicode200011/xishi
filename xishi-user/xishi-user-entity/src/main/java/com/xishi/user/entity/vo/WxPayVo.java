package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WxPayVo extends BaseVo{

    @ApiModelProperty("支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付类型 0：交易  1：实名")
    private int payType = 0;

    @ApiModelProperty("交易订单Id")
    private Long exchangeId = 0L;
}
