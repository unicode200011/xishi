package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "GetTaskReq", description = "获取任务")
public class GetTaskReq {

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("是否购买商品赠送 0否 1是")
    private Integer type =0;
}
