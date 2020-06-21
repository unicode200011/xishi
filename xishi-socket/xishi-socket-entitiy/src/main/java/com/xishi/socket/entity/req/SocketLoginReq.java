package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SocketLoginReq",description = "socket登录请求")
public class SocketLoginReq {
    @ApiModelProperty("登录用户id")
    private Long userId;
}
