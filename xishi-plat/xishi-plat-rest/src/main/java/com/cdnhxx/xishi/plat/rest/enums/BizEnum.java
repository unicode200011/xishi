package com.cdnhxx.xishi.plat.rest.enums;

import lombok.Getter;

/**
 * 业务返回枚举
 *
 * @author: lx
 */
@Getter
public enum BizEnum {

    OK(200, "操作成功"),
    OP_ERROR(10000, "操作错误"),
    USER_NOT_EXIST(10001, "用户不存在或状态异常"),
    REGISTER(10002, "请先注册"),
    BANDING_PHONE(10003, "绑定手机号"),
    NOT_REGISTER(10004, "该手机号未注册"),;

    private Integer code;

    private String msg;


    BizEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
