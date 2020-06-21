package com.xishi.user.entity.mqMessage;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRabbitMessage {
    @ApiModelProperty("消息类型")
    private String type;

    private Object data;

    @ApiModelProperty("用户id")
    private Long userId;
}
