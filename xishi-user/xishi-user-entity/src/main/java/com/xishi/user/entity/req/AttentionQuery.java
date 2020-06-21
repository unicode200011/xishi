package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AttentionQuery", description = "关注查询 ")
public class AttentionQuery extends BaseQuery {

    @ApiModelProperty("查询类型  0：关注  1：粉丝")
    private Integer keyWord;
}
