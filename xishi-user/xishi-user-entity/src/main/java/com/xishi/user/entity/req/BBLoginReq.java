package com.xishi.user.entity.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "BBLoginReq", description = "请求登录参数")
public class BBLoginReq {
    private String xishiNo;
    private Integer type;
    private String orderNum;
    private Integer payType;
    private BigDecimal amount;
}
