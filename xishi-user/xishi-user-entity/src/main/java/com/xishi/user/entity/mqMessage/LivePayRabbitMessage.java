package com.xishi.user.entity.mqMessage;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LivePayRabbitMessage", description = "常规模式计费信息")
public class LivePayRabbitMessage {

    @ApiModelProperty("收费模式 0：常规收费 1：计时收费2:更新用户钱包记录")
    private Integer liveMode = 0;

    @ApiModelProperty("观看分钟数")
    private Integer liveTime = 1;

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("观看用户id")
    private Long userId;

    @ApiModelProperty("直播流名")
    private String streamName;

    @ApiModelProperty("直播间id")
    private Long liveId;

    @ApiModelProperty("主播用户id")
    private Long liveUserId;

    @ApiModelProperty("直播间最新记录id")
    private Long liveRecordId;
}
