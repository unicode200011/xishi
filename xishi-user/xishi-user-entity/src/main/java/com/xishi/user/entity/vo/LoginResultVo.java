package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "loginResultVo", description = "登录结果")
public class LoginResultVo {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("是否首次登录 0 ：否  1：是")
    private Integer firstLogin;

    public LoginResultVo() {

    }

    public LoginResultVo(Long userId,String phone) {
        this.userId=userId;
        this.phone=phone;
    }
}
