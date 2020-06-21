package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentSearchUserVo", description = "家族搜索用户结果")
public class AgentSearchUserVo {

    @ApiModelProperty("用户id")
    private Long Id;

    @ApiModelProperty("西施号")
    private String xishiNum;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("个人简介")
    private String intro;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("状态 0：未发送 1：已发送")
    private Integer state = 0;

    @ApiModelProperty("状态 0 未同意 1 已同意 2 已拒绝")
    private Integer joinState = 0;
}
