package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentInviteReq", description = "家族邀请用户")
public class AgentInviteReq {

    @ApiModelProperty("被邀请用户id 家族邀请/解除邀约 参数")
    private Long linkUserId;

    @ApiModelProperty("家族ID")
    private String agentNum;

    @ApiModelProperty("是否同意 1：同意 2 拒绝  同意加入 参数")
    private Integer joinState;

    @ApiModelProperty("APP 不用传")
    private Long userId;

}
