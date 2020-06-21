package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "FansUserVo", description = "粉丝")
public class FansUserVo {

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

    @ApiModelProperty("是否关注 0 未关注 1 已关注")
    private Integer attention;


}
