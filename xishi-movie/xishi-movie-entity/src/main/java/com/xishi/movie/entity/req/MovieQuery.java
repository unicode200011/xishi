package com.xishi.movie.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MovieQuery", description = "电影搜索条件")
public class MovieQuery{

    @ApiModelProperty("在线观看人数")
    private Integer count;

    @ApiModelProperty("电影id")
    private Long id;

    @ApiModelProperty("增加或减少 0 增加 1：减少")
    private Integer io = 0;

}
