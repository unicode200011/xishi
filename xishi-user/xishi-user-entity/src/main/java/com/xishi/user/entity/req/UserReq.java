package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "UserReq", description = "用户搜索条件")
public class UserReq{

    @ApiModelProperty("用户ID App不用传")
    private Long userId;

    @ApiModelProperty("被查看用户ID")
    private Long linkUserId;

}
