package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "UserGradeVo", description = "等级信息")
public class UserGradeVo {

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("等级标识")
    private String image;

    @ApiModelProperty("等级颜色 格式：#FFFFFF")
    private String color;

    @ApiModelProperty("等级颜色 格式：0,0,0")
    private String rgb;

    @ApiModelProperty("经验值")
    private BigDecimal amount;
}
