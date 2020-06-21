package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "DynamicCommentBackVo", description = "动态评论回复")
public class DynamicCommentBackVo {

    @ApiModelProperty("回复评论id")
    private Long id;

    @ApiModelProperty("评论回复内容")
    private String content;

    @ApiModelProperty("评论回复用户id")
    private Long userId;

    @ApiModelProperty("动态id")
    private Long dynamicId;

    @ApiModelProperty("被回复的评论id")
    private Long parentId;

    @ApiModelProperty("回复用户昵称")
    private String userName;

    @ApiModelProperty("回复用户头像")
    private String avatar;

    @ApiModelProperty("回复+名")
    private String remark;

    @ApiModelProperty("时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("时间")
    private String createTimeStr;
}
