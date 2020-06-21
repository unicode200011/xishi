package com.xishi.user.enums;

import lombok.Getter;

/**
 * 用户等级
 *
 * @author: lx
 */
@Getter
public enum UserLevelEnum {
    Normal(0, "普通"),
    PaiKe(1, "拍客"),
    Manager(2, "经理人"),;

    private Integer code;
    private String msg;

    UserLevelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserLevelEnum getByCode(Integer code) {
        for (UserLevelEnum videoPermEnum : values()) {
            if (videoPermEnum.getCode() == code) {
                return videoPermEnum;
            }
        }
        return null;
    }
}
