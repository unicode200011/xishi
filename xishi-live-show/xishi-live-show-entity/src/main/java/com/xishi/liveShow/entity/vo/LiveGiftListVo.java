package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LiveGiftListVo", description = "直播贡献榜列表信息")
public class LiveGiftListVo {
    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("贡献值")
    private BigDecimal amount;

}
