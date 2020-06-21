package com.xishi.movie.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "MovieCateVo",description = "电影分类")
@Data
public class MovieCateVo {

    @ApiModelProperty("类型ID,进入详情用")
    private Long id;

    @ApiModelProperty("类型名")
    private String name;


}
