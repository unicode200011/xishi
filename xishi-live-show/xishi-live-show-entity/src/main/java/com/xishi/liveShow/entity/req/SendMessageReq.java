package com.xishi.liveShow.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SendMessageReq", description = "禁言")
public class SendMessageReq {

    @ApiModelProperty("直播Id")
    private Long liveId;

    @ApiModelProperty("0:禁言 1：取消禁言")
    private Integer type;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;
}
