package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "SortUserVo", description = "排行榜用户信息")
public class SortUserVo {

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("性别: 0:男1:女")
    private Integer gender;

    @ApiModelProperty("收益值")
    private BigDecimal amount;

    @ApiModelProperty("是否关注 0：未关注 1：已关注")
    private Integer attention;

    @ApiModelProperty("用户id")
    private Long userId;

}
