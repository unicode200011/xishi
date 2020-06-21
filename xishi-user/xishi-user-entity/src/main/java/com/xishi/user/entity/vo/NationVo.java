package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "nationVo", description = "民族")
public class NationVo {

    @ApiModelProperty("民族id")
    private Long id;

    @ApiModelProperty("民族名称")
    private String name;
}
