package com.xishi.liveShow.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LiveReq", description = "创建直播信息")
public class LiveReq {
    @ApiModelProperty("直播封面")
    private String cover;

    @ApiModelProperty("直播标题")
    private String name;

    @ApiModelProperty("直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费")
    private Integer liveMode;

    @ApiModelProperty("房间密码 密码模式有效")
    private String livePwd;

    @ApiModelProperty("西施币 收费模式有效 ")
    private BigDecimal livePrice;

    @ApiModelProperty("用户id APP不用传")
    private Long userId;


}
