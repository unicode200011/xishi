package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinishRealNameVo extends BaseVo{

    @ApiModelProperty("1--认证通过 0--不通过")
    private int isPass;
}
