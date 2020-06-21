package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "WithdrawalVo", description = "提现记录")
public class WithdrawalVo {

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("途径")
    private String accountName;

    @ApiModelProperty(hidden = true)
    private String account;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("状态 0: 待审核 1:待打款 3:驳回4已打款")
    private Integer state;

    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date createTime;
}
