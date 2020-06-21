package com.xishi.user.enums;

import lombok.Getter;

/**
 * 用户合同状态 状态 0: 未生效 1: 生效中 2: 已过期 3: 已过期未结清
 *
 * @author: lx
 */
@Getter
public enum UserContractEnum {
    Not_Invalid(0, "未生效"),
    Ing(1, "生效中"),
    Over(2, "已过期"),
    Over_Not_Finish(3, "已过期未结清"),
    ;

    private Integer code;
    private String msg;

    UserContractEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserContractEnum getByCode(Integer code) {
        for (UserContractEnum videoPermEnum : values()) {
            if (videoPermEnum.getCode() == code) {
                return videoPermEnum;
            }
        }
        return null;
    }
}
