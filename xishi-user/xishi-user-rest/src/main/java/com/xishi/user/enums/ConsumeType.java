package com.xishi.user.enums;

import lombok.Getter;

/**
 * @author: lx
 */
@Getter
public enum ConsumeType {

    Recharge(0, "充值"),
    WithDraw(1, "提现"),
    ChargeNp(2, "牛票充值"),
    InviteChargeNp(3, "邀请好友充值牛票"),
    BuyVip(4, "购买/续费会员"),
    VipShare(5, "会员回馈"),
    AuthFailRefund(6, "实名认证失败退款"),;

    private int code;
    private String sign;

    ConsumeType(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
