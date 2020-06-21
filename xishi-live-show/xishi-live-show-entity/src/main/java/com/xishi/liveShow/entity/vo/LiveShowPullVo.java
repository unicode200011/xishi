package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveShowPullVo", description = "直播相关信息")
public class LiveShowPullVo extends LiveVo {

    @ApiModelProperty("播流地址")
    private String pullUrl;

    @ApiModelProperty("主播头像")
    private String avatar;

    @ApiModelProperty("主播名字")
    private String userName;

    @ApiModelProperty("是否关注0：否 1：是")
    private Integer attention;

    @ApiModelProperty("直播开始时间 毫秒")
    private Long createDateTime;

    @ApiModelProperty("用户id")
    private Long userId;
}
