package com.xishi.user.entity.mqMessage;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "MoviePayRabbitMessage", description = "看电影收费信息")
public class MoviePayRabbitMessage {

    @ApiModelProperty("单价")
    private BigDecimal price;

    @ApiModelProperty("电影名")
    private String name;

    @ApiModelProperty("观看用户id")
    private Long userId;

    @ApiModelProperty("电影id")
    private Long movieId;
}
