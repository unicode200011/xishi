package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@ApiModel(value = "SocketLiveSendMessageReq",description = "发送直播消息请求")
public class SocketLiveSendMessageReq {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户等级")
    private Integer userLevel;

    @ApiModelProperty("用户等级颜色")
    private String rgb;

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("直播流")
    private String streamName;
    public SocketLiveSendMessageReq(){}
}
