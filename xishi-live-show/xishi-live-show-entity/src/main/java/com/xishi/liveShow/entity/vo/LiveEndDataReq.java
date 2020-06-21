package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LiveEndDataReq", description = "直播结束信息")
public class LiveEndDataReq {

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("贡献人数")
    private Integer giveUserNum;

    @ApiModelProperty("观看人数")
    private Long liveWatchNum;

    @ApiModelProperty("直播时长 分钟数")
    private Long liveTime;

    @ApiModelProperty("本场收益")
    private BigDecimal amount;

    @ApiModelProperty("用户id")
    private Long userId;
}
