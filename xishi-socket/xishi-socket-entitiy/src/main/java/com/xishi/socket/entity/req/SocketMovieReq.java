package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@ApiModel(value = "SocketMovieReq",description = "socket看电影请求")
public class SocketMovieReq {
    @ApiModelProperty("登录用户id")
    private Long userId;

    @ApiModelProperty("观看的电影id")
    private Long movieId;
    public SocketMovieReq(){}
}
