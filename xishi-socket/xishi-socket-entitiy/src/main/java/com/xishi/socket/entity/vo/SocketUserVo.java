package com.xishi.socket.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "SocketUserVo",description = "实时更新数据")
public class SocketUserVo {

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户id")
    private Long userId;

}
