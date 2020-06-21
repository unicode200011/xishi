package com.xishi.basic.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "provinceVo", description = "省份")
public class ProvinceVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("省份ID")
    private String provinceid;

    @ApiModelProperty("省份")
    private String province;
}
