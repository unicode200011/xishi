package com.xishi.liveShow.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LiveRoomReq", description = "直播信息")
public class LiveRoomReq {

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费")
    private Integer liveMode;

    @ApiModelProperty("播流类型 默认RTMP 值直接传 RTMP / FLV / HLS")
    private String playType = "RTMP";

    @ApiModelProperty("房间密码 密码模式有效")
    private String livePwd;

    @ApiModelProperty("西施币 收费模式有效 ")
    private BigDecimal livePrice;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;
}
