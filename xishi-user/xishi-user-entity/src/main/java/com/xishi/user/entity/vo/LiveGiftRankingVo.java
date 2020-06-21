package com.xishi.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveGiftRankingVo", description = "")
public class LiveGiftRankingVo {

    @ApiModelProperty("代理人id")
    private Long userId;

    @ApiModelProperty("头像")
    private String avatar;
}
