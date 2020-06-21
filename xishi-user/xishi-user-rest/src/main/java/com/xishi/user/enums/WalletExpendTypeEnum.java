package com.xishi.user.enums;

import lombok.Getter;

/**
 * 用户钱包支出类型
 *
 * @author: lx
 */
@Getter
public enum WalletExpendTypeEnum {

    INCOME(0, "收入"),
    EXPEND(1, "支出");

    private Integer code;
    private String message;

    WalletExpendTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
