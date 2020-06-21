package com.xishi.liveShow.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "LiveListVo", description = "直播列表信息")
public class LiveListVo {
    @ApiModelProperty("直播封面")
    private String cover;

    @ApiModelProperty("直播标题")
    private String name;

    @ApiModelProperty("当前观看人数")
    private Long liveWatchNow;

    @ApiModelProperty("主播名称")
    private String showerName;

    @ApiModelProperty("直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费")
    private Integer liveMode;

    @ApiModelProperty("直播状态 0：正在直播 1：直播结束")
    private Integer state;

    @ApiModelProperty("直播流名")
    private String streamName;

    @ApiModelProperty("直播Id")
    private Long liveId;

    @ApiModelProperty("直播Id")
    private Long id;

    @ApiModelProperty("西施币")
    private BigDecimal livePrice;

    @ApiModelProperty("房间密码 密码模式有效")
    private String livePwd;

}
