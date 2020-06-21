package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "AgentInviteUserVo", description = "家族邀请用户结果")
public class AgentInviteUserVo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("西施号")
    private String xishiNum;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("个人简介")
    private String intro;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("是否加入 0 未同意 1 已同意 2 已拒绝")
    private Integer joinState;

    @ApiModelProperty("状态 0：未发送 1：已发送")
    private Integer state = 1;

    @ApiModelProperty("邀请时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

}
