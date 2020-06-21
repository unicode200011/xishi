package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "UserWalletRecordVo", description = "消费记录")
public class UserWalletRecordVo {

    @ApiModelProperty("西施币")
    private BigDecimal amount;


    @ApiModelProperty("收支方式:0-收入 1-支出")
    private Integer useType;

    @ApiModelProperty("消费类型: 0.充值+ 1.送礼物-  2.获得礼物+")
    private Integer type;

    @ApiModelProperty("用途")
    private String remark;

    @ApiModelProperty("余额")
    private BigDecimal walletAmount;

    @ApiModelProperty("日期")
    @JsonFormat(pattern="yyyy/MM/dd",timezone="GMT+8")
    private Date createTime;
}
