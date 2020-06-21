package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "DynamicCommentReq", description = "动态评论")
public class DynamicCommentReq {

    @ApiModelProperty("动态id")
    @NotEmpty(message = "请传入动态id")
    private Long dynamicId;

    @ApiModelProperty("发布内容")
    @NotEmpty(message = "请传入内容")
    private String content;

    @ApiModelProperty("用户id  app不用传")
    private Long userId;

    @ApiModelProperty("用户id  app不用传")
    private String userName;
}
