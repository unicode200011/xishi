package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "userSimpleVo", description = "简单用户信息")
public class UserSimpleVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("西施号")
    private String accountNo;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("用户总状态 0:正常 1:禁用  2:删除 3:冻结")
    private Integer state;

    @ApiModelProperty("是否实名 0 否 1 是")
    private Integer authed;

    @ApiModelProperty("推荐人id")
    private Long inviterId;

    @ApiModelProperty("推荐人西施号")
    private String inviterAccountNo;

    @ApiModelProperty("等级数")
    private Integer grade;

    @ApiModelProperty("等级名称")
    private String gradeName;

    @ApiModelProperty("冻结结束时间")
    private Date freezeEndTime;

    @ApiModelProperty("冻结开始时间")
    private Date freezeStartTime;

}
