package com.xishi.user.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "invitationCodeVo", description = "邀请码信息")
public class InvitationCodeVo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("邀请码")
    private String code;

    @ApiModelProperty("二维码")
    private String qrCode;

    @ApiModelProperty("分享页地址")
    private String shareUrl;

    @ApiModelProperty("已邀请人数")
    private Integer userNum;
}
