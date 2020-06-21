package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "tuiGuangVo", description = "推广信息")
public class TuiGuangVo {

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String name;

    @ApiModelProperty("邀请码")
    private String code;

    @ApiModelProperty("邀请链接")
    private String url;

}
