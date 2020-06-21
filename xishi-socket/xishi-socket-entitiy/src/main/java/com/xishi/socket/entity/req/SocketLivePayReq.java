package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ApiModel(value = "SocketLivePayReq",description = "触发计时收费")
public class SocketLivePayReq {

    @ApiModelProperty("西施币 价格")
    private BigDecimal livePrice;

    @ApiModelProperty("直播id")
    private Long liveId;

    @ApiModelProperty("直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费")
    private Integer liveMode;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("直播流")
    private String streamName;

    public SocketLivePayReq(){

    }
}
