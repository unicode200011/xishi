package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DynamicQuery", description = "动态列表")
public class DynamicQuery extends BaseQuery {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("列表类型 0：查询自己的  1：看别人的要传userId")
    private Integer sourceType;
}
