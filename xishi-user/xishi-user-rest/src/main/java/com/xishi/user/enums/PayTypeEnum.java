package com.xishi.user.enums;

import lombok.Getter;

/**
 * 消费类型
 *
 * @author: lx
 */
@Getter
public enum PayTypeEnum {

    Gift_Chat_Pay(1, "礼物聊天"),
    Video_Unlock(2, "视频解锁"),
    Video_Comment(3, "视频评论"),
    Mi_Bounty(4, "米赏"),
    Accept_Bounty(5, "应赏"),
    Mi_Bounty_Refund(6, "米赏退回"),
    Gift_Chat_Refund(7, "礼物聊天退回"),
    Charge(8, "充值"),
    Withdraw(9, "提现"),
    Withdraw_Fail(10, "提现失败退回"),
    Buy_Gift(11, "购买礼包"),
    Video_Comment_Refund(12, "视频评论退款"),
    Manager_Cc(13, "经理人抽成"),;

    private int code;
    private String msg;

    PayTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static PayTypeEnum getByCode(int code) {
        for (PayTypeEnum payTypeEnum : values()) {
            if (payTypeEnum.getCode() == code) {
                return payTypeEnum;
            }
        }
        return null;
    }
}
