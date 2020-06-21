package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "cityVo", description = "城市")
public class CityVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("城市ID")
    private String cityid;

    @ApiModelProperty("城市名")
    private String city;

    @ApiModelProperty("省份ID")
    private String provinceid;

}
