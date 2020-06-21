package com.xishi.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SmsTransfer implements Serializable {

    private String phone;

    private Object o;
    /**
     * 发送来源 0 APP 1 web/h5
     */
    private Integer sourceType;

}
