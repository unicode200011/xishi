package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserAttentionQuery", description = "查询是否关注")
public class UserAttentionQuery{

    @ApiModelProperty("关注人")
    private Long userId;
    @ApiModelProperty("被关注人")
    private Long linkUid;

}
