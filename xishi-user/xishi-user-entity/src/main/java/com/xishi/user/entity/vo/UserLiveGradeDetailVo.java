package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "UserLiveGradeDetailVo", description = "用户直播等级信息")
public class UserLiveGradeDetailVo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("当前等级")
    private Integer liveLevel;

    @ApiModelProperty("西施号")
    private String xishiNum;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("等级标识")
    private String image;

    @ApiModelProperty("等级颜色 格式：#FFFFFF")
    private String color;

    @ApiModelProperty("等级颜色 格式：0,0,0")
    private String rgb;

    @ApiModelProperty("当前经验值")
    private Long expNum;

    @ApiModelProperty("总经验值")
    private Long maxExpNum;

    @ApiModelProperty("用户等级说明")
    private List<Object> liveGrades;

}
