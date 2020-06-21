package com.cdnhxx.xishi.plat.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "commonDataVo", description = "公共信息")
public class CommonDataVo {

    @ApiModelProperty("公共信息名称")
    private String name;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("额外信息")
    private String extra = "";
}
