package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel(value = "SocketLiveChangeModeReq",description = "转换直播模式")
public class SocketLiveChangeModeReq {

    @ApiModelProperty("直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费")
    private Integer liveMode;

    @ApiModelProperty("房间密码 密码模式有效")
    private String livePwd;

    @ApiModelProperty("西施币 收费模式有效 ")
    private BigDecimal livePrice;

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("0:禁言 1：取消")
    private Integer type;

    @ApiModelProperty("直播流")
    private String streamName;

    public SocketLiveChangeModeReq(){

    }
}
