package com.xishi.movie.entity.vo;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BarrageVo", description = "弹幕")
public class BarrageVo{
    @ApiModelProperty("时间点  秒")
    private Long point;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("电影id")
    private Long movieId;

    @ApiModelProperty("用户id")
    private Long userId;
}
