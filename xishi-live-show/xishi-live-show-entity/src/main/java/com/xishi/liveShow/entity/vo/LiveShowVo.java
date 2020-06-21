package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveShowVo", description = "直播相关信息")
public class LiveShowVo  extends LiveVo {
    @ApiModelProperty("推流地址")
    private String pushUrl;
}
