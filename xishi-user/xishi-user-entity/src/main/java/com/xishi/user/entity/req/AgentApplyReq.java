package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentApplyReq", description = "家族申请")
public class AgentApplyReq {

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证号")
    private String card;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("正面照")
    private String cardFront;

    @ApiModelProperty("反面照")
    private String cardBack;

    @ApiModelProperty("用户id app不用传")
    private Long userId;

    @ApiModelProperty("家族名称")
    private String agentName;

    @ApiModelProperty("家族logo")
    private String agentLogo;

    @ApiModelProperty("家族简介")
    private String agentIntro;

    @ApiModelProperty("家族ID 重新申请时传")
    private String agentNum;

}
