package com.xishi.user.entity.req;

import com.xishi.user.entity.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "BBPayReq", description = "请求充值")
public class BBPayReq {
    @ApiModelProperty("支付类型 0：支付宝  1：微信")
    private int payType = 0;
}
