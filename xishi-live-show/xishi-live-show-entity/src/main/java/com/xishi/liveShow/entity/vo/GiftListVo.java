package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "GiftListVo", description = "礼物列表信息")
public class GiftListVo {
    @ApiModelProperty("礼物样式")
    private String image;

    @ApiModelProperty("礼物名称")
    private String name;

    @ApiModelProperty("礼物Id")
    private Long id;

    @ApiModelProperty("礼物效果")
    private Integer animation;

    @ApiModelProperty("礼物单价（西施币）")
    private BigDecimal money;
}
