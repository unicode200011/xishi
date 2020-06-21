package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "BBPaySubmitReq", description = "请求充值")
public class BBPaySubmitReq {
    @ApiModelProperty("支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付方式id")
    private int payWayId;

    @ApiModelProperty("充值列表id")
    private Integer chargeId;

    @ApiModelProperty(hidden = true)
    private Long userId;
    @ApiModelProperty(hidden = true)
    private String ip;
}
