package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentShowerVo", description = "家族主播信息")
public class AgentShowerVo {

    @ApiModelProperty("主播姓名")
    private String showerName;

    @ApiModelProperty("主播用户id")
    private Long showerUserId;

    @ApiModelProperty("主播头像")
    private String showerAvatar;

    @ApiModelProperty("主播个性签名")
    private String showerIntro;

    @ApiModelProperty("主播西施号")
    private String showerXishiNum;

    @ApiModelProperty("是否关注 0:否 1：是")
    private Integer attention = 0;

    @ApiModelProperty("家族ID")
    private String agentNum;

}
