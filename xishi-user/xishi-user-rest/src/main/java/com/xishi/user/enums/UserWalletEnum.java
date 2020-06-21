package com.xishi.user.enums;

import lombok.Getter;

/**
 * 用户钱包
 *
 * @author: lx
 */
@Getter
public enum UserWalletEnum {
    Income(0, "收入"),
    Expend(1, "支出"),
    Charge(0, "充值"),
    Withdraw(1, "提现"),
    Np_Charge(2, "牛票充值"),
    Friend_Charge(3, "邀请好友充值牛票返现"),;


    private int code;
    private String msg;


    UserWalletEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
