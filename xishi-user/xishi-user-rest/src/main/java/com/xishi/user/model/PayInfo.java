package com.xishi.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PayInfo {

    private Long userId;

    private Long exchangeId;

    private String orderNo;

    private BigDecimal payMoney;

    private Integer payType;

    //支付宝单号
    private String tradeNo;

    private String buyerPayAmount;

    public PayInfo() {

    }
}
