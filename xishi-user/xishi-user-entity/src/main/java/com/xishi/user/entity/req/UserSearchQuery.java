package com.xishi.user.entity.req;

import com.common.base.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "userSearchQuery", description = "用户搜索条件")
public class UserSearchQuery  extends BaseQuery {

    @ApiModelProperty("关键字")
    private String keyword;

}
