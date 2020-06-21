package com.xishi.movie.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MovieTypeQuery", description = "电影分类")
public class MovieTypeQuery extends BaseQuery {
    @ApiModelProperty("类型id")
    private Integer type;
}
