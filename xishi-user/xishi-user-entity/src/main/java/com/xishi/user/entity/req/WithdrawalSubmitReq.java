package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "WithdrawalSubmitReq", description = "提交申请提现")
public class WithdrawalSubmitReq{
    @ApiModelProperty("用户id  app不用传")
    private Long userId;



    @ApiModelProperty("提现金额")
    private BigDecimal amount;

    @ApiModelProperty("账户id")
    private Integer accountId;


}
