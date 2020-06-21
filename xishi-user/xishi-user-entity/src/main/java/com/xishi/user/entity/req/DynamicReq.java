package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DynamicReq", description = "动态")
public class DynamicReq {

    @ApiModelProperty("动态id  添加时不用传")
    private Long id;

    @ApiModelProperty("操作类型 0：点赞  1：取消点赞")
    private Integer type = 0;

    @ApiModelProperty("发布内容 添加时传")
    private String content;

    @ApiModelProperty("动态图片 多张用逗号隔开  添加时传")
    private String images;

    @ApiModelProperty("视频地址")
    private String videoUrl;

    @ApiModelProperty("视频封面")
    private String videoCover;

    @ApiModelProperty("用户id  app不用传")
    private Long userId;
}
