package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@ApiModel(value = "SocketLiveStopReq",description = "socket直播禁播消息")
public class SocketLiveStopReq {
    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("直播流")
    private String streamName;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String reason;
    public SocketLiveStopReq(){

    }
}
