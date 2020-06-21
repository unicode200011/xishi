package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "DynamicVo", description = "动态")
public class DynamicVo {

    @ApiModelProperty("动态id  添加时不用传")
    private Long id;

    @ApiModelProperty("发布内容 添加时传")
    private String content;

    @ApiModelProperty("动态图片 多张用逗号隔开  添加时传")
    private String images;

    @ApiModelProperty("视频地址")
    private String videoUrl;

    @ApiModelProperty("视频封面")
    private String videoCover;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("是否点赞  0：否 1：是")
    private Integer isPraise;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("时间")
    private String createTimeStr;

    @ApiModelProperty("评论数")
    private Long commentNum;

    @ApiModelProperty("点赞数")
    private Long praiseNum;

    @ApiModelProperty("动态的评论 默认五条")
    private List<DynamicCommentVo> commentVos = new ArrayList<>();
}
