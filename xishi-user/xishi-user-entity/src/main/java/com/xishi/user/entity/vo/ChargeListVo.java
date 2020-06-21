package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "ChargeListVo", description = "充值列表信息")
public class ChargeListVo{

    @ApiModelProperty("西施币数量")
    private Integer xishiNum;

    @ApiModelProperty("人民币数量")
    private BigDecimal rmb;

    @ApiModelProperty("id")
    private Integer id;

}
