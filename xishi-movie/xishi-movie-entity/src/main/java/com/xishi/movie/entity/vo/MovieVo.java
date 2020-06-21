package com.xishi.movie.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "MovieVo",description = "电影信息")
@Data
public class MovieVo {

    @ApiModelProperty("电影ID,进入详情用")
    private Long id;

    @ApiModelProperty("电影名")
    private String name;

    @ApiModelProperty("封面图片")
    private String cover;

    @ApiModelProperty("播放量")
    private Long playNum;

    @ApiModelProperty("当前观看人数")
    private Long watchNum;

    @ApiModelProperty("电影价格")
    private BigDecimal price;

    @ApiModelProperty("播放地址")
    private String url;

    @ApiModelProperty("电影简介")
    private String remark;

    @ApiModelProperty("电影分类")
    private String cateName;

    @ApiModelProperty("电影分类id")
    private Integer cateId;

}
