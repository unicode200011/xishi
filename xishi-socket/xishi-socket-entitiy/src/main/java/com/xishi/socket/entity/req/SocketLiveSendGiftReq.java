package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel(value = "SocketLiveSendGiftReq",description = "发送直播消息请求")
public class SocketLiveSendGiftReq {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("礼物id")
    private Integer giftId;

    @ApiModelProperty("礼物单价")
    private BigDecimal price;

    @ApiModelProperty("礼物名字")
    private String giftName;

    @ApiModelProperty("礼物图片")
    private String image;

    @ApiModelProperty("礼物数量")
    private Integer giftNum;

    @ApiModelProperty("直播流")
    private String streamName;

    @ApiModelProperty("用户等级颜色")
    private String rgb;
    public SocketLiveSendGiftReq(){}
}
