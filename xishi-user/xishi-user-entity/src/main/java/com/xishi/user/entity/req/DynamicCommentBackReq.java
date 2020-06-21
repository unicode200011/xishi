package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "DynamicCommentBackReq", description = "动态评论回复")
public class DynamicCommentBackReq {

    @ApiModelProperty("动态id")
    private Long dynamicId;

    @ApiModelProperty("动态评论id 是主评论id  不是被回复的那条id")
    private Long parentId;

    @ApiModelProperty("发布内容")
    private String content;

    @ApiModelProperty("被回复的用户id")
    private Long backUserId;

    @ApiModelProperty("提交回复的用户id  app不用传")
    private Long userId;

    @ApiModelProperty("提交回复的用户名  app不用传")
    private String userName;
}
