package com.xishi.liveShow.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SendGiftReq", description = "赠送礼物")
public class SendGiftReq {

    @ApiModelProperty("直播Id")
    private Long liveId;

    @ApiModelProperty("礼物id")
    private Long giftId;

    @ApiModelProperty("礼物数量")
    private Integer num;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;
}
