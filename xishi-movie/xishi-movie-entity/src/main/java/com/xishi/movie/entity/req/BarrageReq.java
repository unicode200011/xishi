package com.xishi.movie.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BarrageReq", description = "弹幕")
public class BarrageReq{
    @ApiModelProperty("时间点  秒")
    private Long point;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("电影id")
    private Long movieId;


    @ApiModelProperty("用户id 不用传")
    private Long userId;
}
