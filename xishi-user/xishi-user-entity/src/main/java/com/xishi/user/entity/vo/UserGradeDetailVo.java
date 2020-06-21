package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "UserGradeDetailVo", description = "用户等级信息")
public class UserGradeDetailVo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("当前等级")
    private Integer userLevel;

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
    private BigDecimal expNum;

    @ApiModelProperty("总经验值")
    private BigDecimal maxExpNum;

    @ApiModelProperty("用户等级说明")
    private List<UserGradeVo> userGrades;

}
