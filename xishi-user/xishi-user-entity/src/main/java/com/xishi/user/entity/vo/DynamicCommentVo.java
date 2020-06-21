package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "DynamicCommentVo", description = "动态评论")
public class DynamicCommentVo {

    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("动态id")
    private Long dynamicId;

    @ApiModelProperty("评论用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("时间")
    private String createTimeStr;

    @ApiModelProperty("评论的回复 默认五条")
    private List<DynamicCommentBackVo> commentBackVos = new ArrayList<>();
}
