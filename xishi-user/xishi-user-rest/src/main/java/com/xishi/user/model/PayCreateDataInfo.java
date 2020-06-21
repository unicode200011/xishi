package com.xishi.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PayCreateDataInfo {
    @ApiModelProperty(hidden = true)
    private String orderId;
    @ApiModelProperty(hidden = true)
    private Integer orderType;
    @ApiModelProperty("需跳转的链接地址")
    private String paymentUrl;

    public PayCreateDataInfo() {
    }
}
