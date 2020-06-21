package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ChargeRecordVo", description = "充值记录")
public class ChargeRecordVo {

    /**
     * 充值人民币金额
     */
    @ApiModelProperty("充值人民币金额")
    private BigDecimal rmbAmount;

    /**
     * 西施币数量
     */
    @ApiModelProperty("西施币数量")
    private BigDecimal xishiAmount;

    /**
     * 充值后钱包余额
     */
    @ApiModelProperty("充值后钱包余额")
    private BigDecimal walletAmount;

    /**
     * 充值途径 0微信 1支付宝
     */
    @ApiModelProperty("充值途径 0微信 1支付宝")
    private Integer source;

    @ApiModelProperty("充值途径")
    private String sourceName;

    /**
     * 充值时间
     */
    @ApiModelProperty("充值时间")
    @JsonFormat(pattern="yyyy/MM/dd",timezone="GMT+8")
    private Date createTime;
}
