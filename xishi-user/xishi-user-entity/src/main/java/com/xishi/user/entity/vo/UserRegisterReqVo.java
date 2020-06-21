package com.xishi.user.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by root on 9/12/18.
 */
@Setter
@Getter
@ToString
public class UserRegisterReqVo {

    private String phone;

    private String password;
    /**
     * 邀请码
     */
    private String code;
    /**
     * 短信验证码
     */
//    @NotBlank
//    private String inCode;

}
