package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LiveCheckPayVo", description = "直播相关信息")
public class LiveCheckPayVo extends LiveVo {

    @ApiModelProperty("是否支付0：否 1：是")
    private Integer isPayed;

    @ApiModelProperty("是否输入密码0：否 1：是")
    private Integer isPwd;

}
