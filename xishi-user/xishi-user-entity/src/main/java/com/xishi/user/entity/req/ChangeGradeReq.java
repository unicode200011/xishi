package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "changeGradeReq", description = "改变等级")
public class ChangeGradeReq {

    @ApiModelProperty("等级数")
    private Integer grade;

    @ApiModelProperty("等级名称")
    private String gradeName;

    @ApiModelProperty("用户id")
    private Long userId;
}
