package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentApplyVo", description = "家族申请信息")
public class AgentApplyVo {

    @ApiModelProperty("代理人id")
    private Long userId;

    @ApiModelProperty("代理人姓名")
    private String agentUserName;

    @ApiModelProperty("代理人头像")
    private String agentUserAvatar;

    @ApiModelProperty("代理人个性签名")
    private String agentUserIntro;

    @ApiModelProperty("代理人西施号")
    private String agentUserXishiNum;

    @ApiModelProperty("是否关注 0:否 1：是")
    private Integer attention = 0;

    @ApiModelProperty("家族ID")
    private String agentNum;

    @ApiModelProperty("家族名称")
    private String agentName;

    @ApiModelProperty("家族logo")
    private String agentLogo;

    @ApiModelProperty("家族简介")
    private String agentIntro;

    @ApiModelProperty("家族签约主播数量")
    private Integer showerNum;

    @ApiModelProperty("审核状态 0：待审核 1：通过 2：驳回")
    private Integer auditState;

    @ApiModelProperty("身份证正面照")
    private String cardFront;

    @ApiModelProperty("身份证反面照")
    private String cardBack;

    @ApiModelProperty("身份证号")
    private String card;

    @ApiModelProperty("真实姓名")
    private String realName;
}
