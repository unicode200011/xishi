package com.xishi.user.entity.mqMessage;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LiveGiftRabbitMessage", description = "礼物信息")
public class LiveGiftRabbitMessage {

    @ApiModelProperty("赠送数量")
    private Integer num;

    @ApiModelProperty("礼物单价")
    private BigDecimal price;

    @ApiModelProperty("礼物总价")
    private BigDecimal giftMoney;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("被赠送用户id")
    private Long linkUid;

    @ApiModelProperty("直播流名")
    private String streamName;

    @ApiModelProperty("直播间id")
    private Long liveId;

    @ApiModelProperty("直播间最新记录id")
    private Long liveRecordId;

    @ApiModelProperty("礼物id")
    private String giftName;

    @ApiModelProperty("礼物id")
    private Long id;
}
