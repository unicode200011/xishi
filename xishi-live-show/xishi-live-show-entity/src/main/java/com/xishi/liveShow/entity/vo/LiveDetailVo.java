package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveDetailVo", description = "直播信息")
public class LiveDetailVo {
    @ApiModelProperty("直播Id")
    private Long id;

    @ApiModelProperty("直播Id")
    private Long userId;

    @ApiModelProperty("直播流名")
    private String streamName;

    @ApiModelProperty("最新直播记录id")
    private Long newLiveRecord;

    @ApiModelProperty("直播房间状态  0：正常  1：禁播")
    private Integer state;

    @ApiModelProperty("直播状态 0：未直播 1：正在直播")
    private Integer liveState;

    @ApiModelProperty("禁播原因")
    private String reason;
}
