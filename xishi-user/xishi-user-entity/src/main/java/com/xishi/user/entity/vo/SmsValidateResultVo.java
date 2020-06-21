package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "smsValidateResultVo", description = "短信验证结果")
public class SmsValidateResultVo {

    @ApiModelProperty("是否注册")
    public boolean register;

    public SmsValidateResultVo() {

    }

    public SmsValidateResultVo(boolean register) {
        this.register=register;
    }
}
