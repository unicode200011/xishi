package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BankUserReq", description = "银行账户")
public class BankUserReq{

    @ApiModelProperty("银行账户id 更新是传入")
    private Long id;

    @ApiModelProperty("账户类型id")
    private Long bankAccountId;

    @ApiModelProperty("账户号码")
    private String bankCard;

    @ApiModelProperty("账户二维码")
    private String qrcode;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;

}
