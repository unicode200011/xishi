package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "BBResultVo", description = "币宝登录货币交易返回信息")
public class BBResultVo extends BaseVo{

    @ApiModelProperty("支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付类型 0：支付宝  1：微信")
    private int payType = 0;

    @ApiModelProperty("交易订单号")
    private String orderNum;

    @ApiModelProperty("充值列表id")
    private Integer chargeId;

    @ApiModelProperty("登陆地址")
    private String Url;

    @ApiModelProperty("Token")
    private String Token;

}
