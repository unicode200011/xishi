package com.xishi.user.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "userVo", description = "用户信息")
public class UserVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("区县")
    private String county;

    @ApiModelProperty("性别: 0:男 1:女  2:保密")
    private Integer gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("个人简介")
    private String intro;

    @ApiModelProperty("生日,格式yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date birthday;

    @ApiModelProperty("职业")
    private String job;

}
