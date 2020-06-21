package com.xishi.user.entity.vo;


import lombok.Data;

/**
 * 认证的请求dto
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:00
 */
@Data
public class RealNameAuditReqVo {

    private Long userId;
    private String password;
    private String newPassword;
    private String loginName;

}
