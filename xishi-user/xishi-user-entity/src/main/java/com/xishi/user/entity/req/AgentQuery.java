package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AgentQuery", description = "家族查询 ")
public class AgentQuery extends BaseQuery {

    @ApiModelProperty("名称或编号 搜索的时候传")
    private String keyWord;
}
