package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "AgentInviteAgentVo", description = "家族邀请用户结果")
public class AgentInviteAgentVo {

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

    @ApiModelProperty("是否加入 0 未同意 1 已同意")
    private Integer joinState;

    @ApiModelProperty("邀请时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

}
