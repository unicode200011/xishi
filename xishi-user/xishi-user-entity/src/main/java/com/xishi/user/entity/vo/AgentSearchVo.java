package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentSearchVo", description = "家族搜索结果")
public class AgentSearchVo {

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

}
