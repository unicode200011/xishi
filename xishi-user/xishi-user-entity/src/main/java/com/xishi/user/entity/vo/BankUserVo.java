package com.xishi.user.entity.vo;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BankUserVo", description = "用户银行账户")
public class BankUserVo{

    @ApiModelProperty("账户类型id")
    private Long bankAccountId;

    @ApiModelProperty("账户类型名")
    private String bankAccountName;

    @ApiModelProperty("账户id")
    private Long id;

    @ApiModelProperty("账户号码")
    private String bankCard;

    @ApiModelProperty("账户二维码")
    private String qrcode;

    @ApiModelProperty("用户id")
    private Long userId;

}
