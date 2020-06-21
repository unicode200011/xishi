package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "bankCardVo", description = "银行卡信息")
public class BankCardVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("持卡人姓名")
    private String userName;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行卡logo")
    private String bankLogo;

    @ApiModelProperty("银行卡号")
    private String cardNum;

    @ApiModelProperty("卡类型 0:储存卡 1:信用卡")
    private Integer cardType;

    @ApiModelProperty("开户行")
    private String openBank;
}
