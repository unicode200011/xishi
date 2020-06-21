package com.xishi.socket.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlayMovieReq",description = "播放电影请求")
public class PlayMovieReq {

    @ApiModelProperty("播放的电影id")
    private Long movieId;
    public PlayMovieReq(){

    }
}
