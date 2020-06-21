package com.xishi.user.enums;

import lombok.Getter;

/**
 * 平台收支类型
 *
 * @author: lx
 */
@Getter
public enum PlatUseTypeEnum {
    Charge(0, "充值"),
    Buy_Gift(1, "礼包购买"),
    Withdraw(2, "提现");


    private int code;
    private String msg;

    PlatUseTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
