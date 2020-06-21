package com.xishi.user.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PayResponInfo {
    @ApiModelProperty("响应状态")
    private Integer code;
    @ApiModelProperty("响应信息")
    private String message;
    @ApiModelProperty("响应签名")
    private String signature;
    @ApiModelProperty("响应内容")
    private PayCreateDataInfo data;

    public PayResponInfo() {
    }
}
