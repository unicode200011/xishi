package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AttentionReq", description = "关注用户")
public class AttentionReq {

    @ApiModelProperty("被关注用户id")
    private Long linkUserId;

    @ApiModelProperty("APP 不用传")
    private Long userId;

    @ApiModelProperty("0：添加关注  1：取消关注")
    private Integer type = 0;

}
