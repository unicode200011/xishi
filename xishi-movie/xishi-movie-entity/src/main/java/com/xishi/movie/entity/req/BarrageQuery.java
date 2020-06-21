package com.xishi.movie.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BarrageQuery", description = "弹幕查询")
public class BarrageQuery extends BaseQuery {

    @ApiModelProperty("电影id")
    private Long movieId;
}
