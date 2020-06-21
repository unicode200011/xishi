package com.xishi.user.enums;

import lombok.Getter;

/**
 * 业务返回枚举
 *
 * @author: lx
 */
@Getter
public enum ApplyAgentEnum {

    NOT_APPLY(0, "未申请"),
    PASSS_APPLY(1, "通过"),
    REFUSE_APPLY(2, "驳回"),
    ING_APPLY(3, "申请中"),;

    private Integer code;

    private String msg;


    ApplyAgentEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
