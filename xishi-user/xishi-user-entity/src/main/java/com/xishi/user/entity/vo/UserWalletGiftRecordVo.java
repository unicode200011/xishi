package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "UserWalletGiftRecordVo", description = "收礼明细记录")
public class UserWalletGiftRecordVo{

    @ApiModelProperty("礼物名字")
    private String custName;


    @ApiModelProperty("西施币数量")
    private BigDecimal amount;

    @ApiModelProperty("礼物数量")
    private Integer custNum;

    @ApiModelProperty("送礼物人的昵称")
    private String userName;

    @ApiModelProperty("送礼物人id")
    private Long linkUid;

    @ApiModelProperty("余额")
    private BigDecimal walletAmount;
    @ApiModelProperty("消费类型:  0.充值+  1.送礼物-  2.获得礼物+ 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号 8：提现")
    private Integer type;
    @ApiModelProperty("日期")
    @JsonFormat(pattern="yyyy/MM/dd",timezone="GMT+8")
    private Date createTime;
}
